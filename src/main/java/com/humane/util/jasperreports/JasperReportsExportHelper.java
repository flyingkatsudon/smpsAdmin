package com.humane.util.jasperreports;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class JasperReportsExportHelper {
    private static final String XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    private static final String XLS = "application/vnd.ms-excel";
    private static final String PDF = "application/pdf";
    public static final String EXT_PDF = "pdf";
    public static final String EXT_XLS = "xls";
    public static final String EXT_XLSX = "xlsx";


    private static JasperReportsExportHelper instance;

    static {
        instance = new JasperReportsExportHelper();
    }

    public static ResponseEntity toResponseEntity(String viewName, String format, List<?> content) {
        JRRewindableDataSource dataSource = (content == null || content.size() == 0) ? new JREmptyDataSource() : new JRBeanCollectionDataSource(content);
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            JasperPrint jasperPrint = instance.getJasperPrint(viewName, dataSource);

            if (format.equals(EXT_PDF)) {
                instance.exportReportToPdf(jasperPrint, baos);
                HttpHeaders headers = new HttpHeaders();
                headers.set("Set-Cookie", "fileDownload=true; path=/");
                headers.setContentType(MediaType.parseMediaType(PDF));
                headers.set("Content-Disposition", encode("inline", jasperPrint.getName() + "." + EXT_PDF));
                return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.OK);
            } else if (format.equals(EXT_XLS)) {
                instance.exportReportToXls(jasperPrint, baos);
                HttpHeaders headers = new HttpHeaders();
                headers.set("Set-Cookie", "fileDownload=true; path=/");
                headers.setContentType(MediaType.parseMediaType(XLS));
                headers.set("Content-Disposition", encode("attachment", jasperPrint.getName() + "." + XLS));
                return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.OK);
            } else if (format.equals(EXT_XLSX)) {
                instance.exportReportToXlsx(jasperPrint, baos);
                HttpHeaders headers = new HttpHeaders();
                headers.set("Set-Cookie", "fileDownload=true; path=/");
                headers.setContentType(MediaType.parseMediaType(XLSX));
                headers.set("Content-Disposition", encode("attachment", jasperPrint.getName() + "." + EXT_XLSX));
                return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.OK);
            }
        } catch (JRException | IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    public static File toXlsxFile(String viewName, List<?> content) {
        return toFile((File) null, viewName, EXT_XLSX, content);
    }

    public static File toXlsFile(String viewName, List<?> content) {
        return toFile((File) null, viewName, EXT_XLS, content);
    }

    public static File toPdfFile(String viewName, List<?> content) {
        return toFile((File) null, viewName, EXT_PDF, content);
    }

    public static File toFile(String viewName, String format, List<?> content) {
        return toFile((File) null, viewName, format, content);
    }

    public static File toFile(String path, String viewName, String format, List<?> content) {
        return toFile(new File(path), viewName, format, content);
    }

    public static File toFile(File path, String viewName, String format, List<?> content) {
        JRRewindableDataSource dataSource = (content == null || content.size() == 0) ? new JREmptyDataSource() : new JRBeanCollectionDataSource(content);

        FileOutputStream fos = null;
        File file = null;
        try {
            JasperPrint jasperPrint = instance.getJasperPrint(viewName, dataSource);
            if (jasperPrint == null) return null;

            if (path != null) {
                path.mkdirs();
                file = new File(path, jasperPrint.getName() + "." + format);
            } else {
                file = new File(jasperPrint.getName() + "." + format);
            }

            fos = new FileOutputStream(file);

            if (format.equals(EXT_PDF)) {
                instance.exportReportToPdf(jasperPrint, fos);
            } else if (format.equals(EXT_XLS)) {
                instance.exportReportToXls(jasperPrint, fos);
            } else if (format.equals(EXT_XLSX)) {
                instance.exportReportToXlsx(jasperPrint, fos);
            }
        } catch (JRException | FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(fos);
        }
        return file;
    }

    private void exportReportToPdf(JasperPrint jasperPrint, OutputStream outputStream) throws JRException {
        JRPdfExporter exporter = new JRPdfExporter(DefaultJasperReportsContext.getInstance());
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
        exporter.exportReport();
    }

    private void exportReportToXlsx(JasperPrint jasperPrint, OutputStream outputStream) throws JRException {
        JRXlsxExporter exporter = new JRXlsxExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));

        SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
        exporter.setConfiguration(configuration);

        exporter.exportReport();
    }

    private void exportReportToXls(JasperPrint jasperPrint, OutputStream outputStream) throws JRException {
        JRXlsExporter exporter = new JRXlsExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));

        SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
        exporter.setConfiguration(configuration);

        exporter.exportReport();
    }

    private JasperPrint getJasperPrint(String viewName, JRRewindableDataSource dataSource) throws JRException {
        return getJasperPrint(viewName, new LinkedHashMap<>(), dataSource);
    }

    private JasperPrint getJasperPrint(String viewName, Map<String, Object> params, JRRewindableDataSource dataSource) throws JRException {
        JasperReport jasperReport = loadReport(viewName);
        if (jasperReport == null) return null;

        return JasperFillManager.fillReport(jasperReport, params, dataSource);
    }

    private JasperReport loadReport(String viewName) {
        try (InputStream inputStream = JasperReportsExportHelper.class.getClassLoader().getResourceAsStream(viewName)) {
            JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
            return JasperCompileManager.compileReport(jasperDesign);
        } catch (IOException | JRException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encode(String type, String filename) {
        StringBuilder contentDisposition = new StringBuilder(type);
        CharsetEncoder enc = StandardCharsets.US_ASCII.newEncoder();
        boolean canEncode = enc.canEncode(filename);
        if (canEncode) {
            contentDisposition.append("; filename=").append('"').append(filename).append('"');
        } else {
            enc.onMalformedInput(CodingErrorAction.IGNORE);
            enc.onUnmappableCharacter(CodingErrorAction.IGNORE);

            String normalizedFilename = Normalizer.normalize(filename, Normalizer.Form.NFKD);
            CharBuffer cbuf = CharBuffer.wrap(normalizedFilename);

            ByteBuffer bbuf;
            try {
                bbuf = enc.encode(cbuf);
            } catch (CharacterCodingException e) {
                bbuf = ByteBuffer.allocate(0);
            }

            String encodedFilename = new String(bbuf.array(), bbuf.position(), bbuf.limit(), StandardCharsets.US_ASCII).trim();

            if (!encodedFilename.equals("")) {
                contentDisposition.append("; filename=").append('"').append(encodedFilename).append('"');
            }

            URI uri;
            try {
                uri = new URI(null, null, filename, null);
            } catch (URISyntaxException e) {
                uri = null;
            }

            if (uri != null) {
                contentDisposition.append("; filename*=UTF-8''").append(uri.toASCIIString());
            }
        }
        return contentDisposition.toString();
    }
}
