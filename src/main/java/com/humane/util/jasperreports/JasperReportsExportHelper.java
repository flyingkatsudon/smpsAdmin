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
import java.util.Collection;
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

    public static ResponseEntity<byte[]> toResponseEntity(HttpServletResponse response, String viewName, String format, List<?> content) {
        try {
            switch (format) {
                case EXT_PDF:
                    return instance.toPdf(response, viewName, content);
                case EXT_XLS:
                    return instance.toXls(response, viewName, content);
                case EXT_XLSX:
                    return instance.toXlsx(response, viewName, content);
                default:
                    return null;
            }
        } catch (JRException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    private ResponseEntity<byte[]> toXlsx(HttpServletResponse response, String viewName, Collection<?> collection) throws JRException {
        return toXlsx(response, viewName, new LinkedHashMap<>(), collection);
    }

    private ResponseEntity<byte[]> toXlsx(HttpServletResponse response, String viewName, Map<String, Object> params, Collection<?> collection) throws JRException {
        JasperReport jasperReport = loadReport(viewName);
        if (jasperReport == null) return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(collection));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JRXlsxExporter exporter = new JRXlsxExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));

        SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
        exporter.setConfiguration(configuration);

        exporter.exportReport();

        byte[] ba = baos.toByteArray();

        setResponseHeaders(response, XLSX, jasperPrint.getName(), EXT_XLSX);
        return ResponseEntity.ok(ba);
    }

    private ResponseEntity<byte[]> toXls(HttpServletResponse response, String viewName, Collection<?> collection) throws JRException {
        return toXls(response, viewName, new LinkedHashMap<>(), collection);
    }

    private ResponseEntity<byte[]> toXls(HttpServletResponse response, String viewName, Map<String, Object> params, Collection<?> collection) throws JRException {
        JasperReport jasperReport = loadReport(viewName);
        if (jasperReport == null) return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);

        if (params == null) params = new LinkedHashMap<>();

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(collection));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JRXlsExporter exporter = new JRXlsExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));

        SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
        exporter.setConfiguration(configuration);

        exporter.exportReport();

        byte[] ba = baos.toByteArray();

        setResponseHeaders(response, XLS, jasperPrint.getName(), EXT_XLS);
        return ResponseEntity.ok(ba);
    }

    private ResponseEntity<byte[]> toPdf(HttpServletResponse response, String viewName, Collection<?> collection) throws JRException {
        return toPdf(response, viewName, new LinkedHashMap<>(), collection);
    }

    private ResponseEntity<byte[]> toPdf(HttpServletResponse response, String viewName, Map<String, Object> params, Collection<?> collection) throws JRException {
        JasperReport jasperReport = loadReport(viewName);
        if (jasperReport == null) return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(collection));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, baos);
        byte[] ba = baos.toByteArray();

        HttpHeaders header = httpHeaders(PDF, jasperPrint.getName(), EXT_PDF);
        return new ResponseEntity<>(ba, header, HttpStatus.OK);
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
}
