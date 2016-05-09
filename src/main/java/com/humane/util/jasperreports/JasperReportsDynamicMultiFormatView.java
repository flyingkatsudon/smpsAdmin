package com.humane.util.jasperreports;

import com.humane.util.file.FileNameEncoder;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JasperReport;
import org.springframework.web.servlet.view.jasperreports.JasperReportsMultiFormatView;

import java.util.Properties;

/**
 * example
 * @Bean public JasperReportsViewResolver jasperReportsViewResolver() {
 * JasperReportsViewResolver resolver = new JasperReportsViewResolver();
 * resolver.setPrefix("classpath:jrxml/");
 * resolver.setReportDataKey("datasource");
 * resolver.setViewNames("*.jrxml");
 * Properties headers = new Properties();
 * headers.put("Set-Cookie", "fileDownload=true; path=/");
 * headers.put("Content-Transfer-Encoding", "binary");
 * headers.put("X-Frame-Options", " SAMEORIGIN");
 * resolver.setHeaders(headers);
 * resolver.setViewClass(JasperReportsDynamicMultiFormatView.class);
 * resolver.setOrder(0);
 * return resolver;
 * }
 */
@Slf4j
public class JasperReportsDynamicMultiFormatView extends JasperReportsMultiFormatView {

    private static final String FILE_EXT_CSV = "csv";
    private static final String FILE_EXT_XLS = "xls";
    private static final String FILE_EXT_XLSX = "xlsx";
    private static final String FILE_EXT_HTML = "html";
    private static final String FILE_EXT_PDF = "pdf";

    /**
     * The JasperReport that is used to render the view.
     */
    private JasperReport jasperReport;

    /**
     * The last modified time of the jrxml resource file, used to force compilation.
     */
    private long jrxmlTimestamp;

    @Override
    protected void onInit() {
        jasperReport = super.getReport();
        this.setContentDispositionHeader(jasperReport.getName());
        try {
            String url = getUrl();
            if (url != null) {
                jrxmlTimestamp = getApplicationContext().getResource(url).getFile().lastModified();
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    protected JasperReport getReport() {
        if (this.isDirty()) {
            log.info("Forcing recompilation of jasper report as the jrxml has changed");
            this.jasperReport = this.loadReport();
        }
        return this.jasperReport;
    }

    /**
     * Determines if the jrxml file is dirty by checking its timestamp.
     *
     * @return true to force recompilation because the report xml has changed, false otherwise
     */
    private boolean isDirty() {
        long curTimestamp = 0L;
        try {
            String url = getUrl();
            if (url != null) {
                curTimestamp = getApplicationContext().getResource(url).getFile().lastModified();
                if (curTimestamp > jrxmlTimestamp) {
                    jrxmlTimestamp = curTimestamp;
                    return true;
                }
            }
        } catch (Exception ignored) {
        }
        return false;
    }

    /**
     * This will configure the content disposition mapping with the report name.
     */
    protected void setContentDispositionHeader(String reportName) {
        String contentDisp = FileNameEncoder.encode(reportName) + ".";
        Properties mappings = new Properties();
        mappings.put(FILE_EXT_CSV, contentDisp + FILE_EXT_CSV);
        mappings.put(FILE_EXT_HTML, contentDisp + FILE_EXT_HTML);
        mappings.put(FILE_EXT_PDF, contentDisp + FILE_EXT_PDF);
        mappings.put(FILE_EXT_XLS, contentDisp + FILE_EXT_XLS);
        mappings.put(FILE_EXT_XLSX, contentDisp + FILE_EXT_XLSX);
        setContentDispositionMappings(mappings);
    }
}