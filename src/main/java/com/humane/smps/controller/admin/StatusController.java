package com.humane.smps.controller.admin;

import com.humane.smps.dto.*;
import com.humane.smps.mapper.StatusMapper;
import com.humane.util.jasperreports.JasperReportsExportHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "status", method = RequestMethod.GET)
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StatusController {
    private static final String JSON = "json";
    private final StatusMapper mapper;

    @RequestMapping(value = "all")
    public ResponseEntity all(StatusDto param) {
        log.debug("{}", param);
        return ResponseEntity.ok(mapper.all(param));
    }

    @RequestMapping(value = "dept.{format:json|pdf|xls|xlsx}")
    public ResponseEntity dept(@PathVariable String format, StatusDeptDto param, Pageable pageable) {
        switch (format) {
            case JSON:
                return ResponseEntity.ok(mapper.dept(param, pageable));
            default:
                return JasperReportsExportHelper.toResponseEntity(
                        "jrxml/status-dept.jrxml"
                        , format
                        , mapper.dept(param, pageable).getContent());
        }
    }

    @RequestMapping(value = "major.{format:json|pdf|xls|xlsx}")
    public ResponseEntity major(@PathVariable String format, StatusMajorDto param, Pageable pageable) {
        switch (format) {
            case JSON:
                return ResponseEntity.ok(mapper.major(param, pageable));
            default:
                return JasperReportsExportHelper.toResponseEntity(
                        "jrxml/status-major.jrxml"
                        , format
                        , mapper.major(param, pageable).getContent());
        }
    }

    @RequestMapping(value = "hall.{format:json|pdf|xls|xlsx}")
    public ResponseEntity hall(@PathVariable String format, StatusHallDto param, Pageable pageable) {
        switch (format) {
            case JSON:
                return ResponseEntity.ok(mapper.hall(param, pageable));
            default:
                return JasperReportsExportHelper.toResponseEntity(
                        "jrxml/status-hall.jrxml"
                        , format
                        , mapper.hall(param, pageable).getContent());
        }
    }

    @RequestMapping(value = "group.{format:json|pdf|xls|xlsx}")
    public ResponseEntity group(@PathVariable String format, StatusGroupDto param, Pageable pageable) {
        switch (format) {
            case JSON:
                return ResponseEntity.ok(mapper.group(param, pageable));
            default:
                return JasperReportsExportHelper.toResponseEntity(
                        "jrxml/status-group.jrxml"
                        , format
                        , mapper.group(param, pageable).getContent());
        }
    }
}