package com.humane.smps.controller;

import com.humane.util.jasperreports.JasperReportsExportHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "download")
@Slf4j
//@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DownloadController {

    @RequestMapping(value = "item", method = RequestMethod.GET)
    public ResponseEntity item(HttpServletResponse response) {
        return JasperReportsExportHelper.toResponseEntity(response,
                "jrxml/setting-item.jrxml",
                "xlsx",
                null
        );
    }

    @RequestMapping(value = "hall", method = RequestMethod.GET)
    public ResponseEntity hall(HttpServletResponse response) {
        return JasperReportsExportHelper.toResponseEntity(response,
                "jrxml/setting-hall.jrxml",
                "xlsx",
                null
        );
    }

    @RequestMapping(value = "examinee", method = RequestMethod.GET)
    public ResponseEntity examinee(HttpServletResponse response) {
        return JasperReportsExportHelper.toResponseEntity(response,
                "jrxml/setting-examinee.jrxml",
                "xlsx",
                null
        );
    }
}