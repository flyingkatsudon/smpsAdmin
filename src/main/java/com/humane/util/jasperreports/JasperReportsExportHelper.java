package com.humane.util.jasperreports;

import com.humane.util.file.FileNameEncoder;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

    public static ResponseEntity<byte[]> toResponseEntity(String viewName, String format, List<?> content) {
        JRRewindableDataSource dataSource = (content == null || content.size() == 0) ? new JREmptyDataSource() : new JRBeanCollectionDataSource(content);
        try {
            switch (format) {
                case EXT_PDF:
                    return instance.toPdf(viewName, dataSource);
                case EXT_XLS:
                    return instance.toXls(viewName, dataSource);
                case EXT_XLSX:
                    return instance.toXlsx(viewName, dataSource);
                default:
                    return null;
            }
        } catch (JRException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    private ResponseEntity<byte[]> toXlsx(String viewName, JRRewindableDataSource dataSource) throws JRException {
        return toXlsx(viewName, new LinkedHashMap<>(), dataSource);
    }

    private ResponseEntity<byte[]> toXlsx(String viewName, Map<String, Object> params, JRRewindableDataSource dataSource) throws JRException {
        JasperReport jasperReport = loadReport(viewName);
        if (jasperReport == null) return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JRXlsxExporter exporter = new JRXlsxExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));

        SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
        exporter.setConfiguration(configuration);

        exporter.exportReport();

        byte[] ba = baos.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Set-Cookie", "fileDownload=true; path=/");
        headers.setContentLength(ba.length);
        headers.setContentType(MediaType.parseMediaType(XLSX));
        headers.set("Content-Disposition", encode("attachment", jasperPrint.getName() + "." + EXT_XLSX));
        return new ResponseEntity<>(ba, headers, HttpStatus.OK);
    }

    private ResponseEntity<byte[]> toXls(String viewName, JRRewindableDataSource dataSource) throws JRException {
        return toXls(viewName, new LinkedHashMap<>(), dataSource);
    }

    private ResponseEntity<byte[]> toXls(String viewName, Map<String, Object> params, JRRewindableDataSource dataSource) throws JRException {
        JasperReport jasperReport = loadReport(viewName);
        if (jasperReport == null) return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);

        if (params == null) params = new LinkedHashMap<>();

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JRXlsExporter exporter = new JRXlsExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));

        SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
        exporter.setConfiguration(configuration);

        exporter.exportReport();

        byte[] ba = baos.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Set-Cookie", "fileDownload=true; path=/");
        headers.setContentLength(ba.length);
        headers.setContentType(MediaType.parseMediaType(XLS));
        headers.set("Content-Disposition", encode("attachment", jasperPrint.getName() + "." + XLS));
        return new ResponseEntity<>(ba, headers, HttpStatus.OK);
    }

    private ResponseEntity<byte[]> toPdf(String viewName, JRRewindableDataSource dataSource) throws JRException {
        return toPdf(viewName, new LinkedHashMap<>(), dataSource);
    }

    private ResponseEntity<byte[]> toPdf(String viewName, Map<String, Object> params, JRRewindableDataSource dataSource) throws JRException {
        JasperReport jasperReport = loadReport(viewName);
        if (jasperReport == null) return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);

        byte[] ba = JasperExportManager.exportReportToPdf(jasperPrint);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Set-Cookie", "fileDownload=true; path=/");
        headers.setContentLength(ba.length);
        headers.setContentType(MediaType.parseMediaType(PDF));
        headers.set("Content-Disposition", encode("inline", jasperPrint.getName() + "." + EXT_PDF));
        return new ResponseEntity<>(ba, headers, HttpStatus.OK);
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


    private void setResponseHeaders(HttpServletResponse response, String contentType, String fileName, String extension) {
        response.setContentType(contentType);
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader("Set-Cookie", "fileDownload=true; path=/");
        response.setHeader("X-Frame-Options", " SAMEORIGIN");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", FileNameEncoder.encode(fileName) + "." + extension);
    }

    private HttpHeaders httpHeaders(String contentType, String fileName, String extension) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(contentType));
        headers.set("Content-Transfer-Encoding", "binary");
        headers.set("Set-Cookie", "fileDownload=true; path=/");
        headers.set("X-Frame-Options", " SAMEORIGIN");
        String enc = FileNameEncoder.encode(fileName);
        headers.set("Content-Disposition", contentType.equals(PDF) ? enc.replace("attachment", "inline") : enc + "." + extension);
        return headers;
    }

    public String encode(String type, String filename) {
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
