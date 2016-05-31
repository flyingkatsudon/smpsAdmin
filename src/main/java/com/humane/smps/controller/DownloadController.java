package com.humane.smps.controller;

import com.humane.util.jasperreports.JasperReportsExportHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "download")
@Slf4j
public class DownloadController {

    @RequestMapping(value = "item.xlsx", method = RequestMethod.GET)
    public ResponseEntity item() {
        return JasperReportsExportHelper.toResponseEntity(
                "jrxml/upload-item.jrxml",
                "xlsx",
                null
        );
    }

    @RequestMapping(value = "hall.xlsx", method = RequestMethod.GET)
    public ResponseEntity hall() {
        return JasperReportsExportHelper.toResponseEntity(
                "jrxml/upload-hall.jrxml",
                "xlsx",
                null
        );
    }

    @RequestMapping(value = "examinee.xlsx", method = RequestMethod.GET)
    public ResponseEntity examinee() {
        return JasperReportsExportHelper.toResponseEntity(
                "jrxml/upload-examinee.jrxml",
                "xlsx",
                null
        );
    }

    @RequestMapping(value = "devi.xlsx", method = RequestMethod.GET)
    public ResponseEntity devi() {
        return JasperReportsExportHelper.toResponseEntity(
                "jrxml/upload-devi.jrxml",
                "xlsx",
                null
        );
    }
}