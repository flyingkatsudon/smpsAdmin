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

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "status", method = RequestMethod.GET)
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StatusController {
    private static final String CHART = "chart";
    private static final String LIST = "list";
    private final StatusMapper mapper;

    @RequestMapping(value = "all")
    public StatusDto all(StatusDto param) {
        return mapper.findAll(param);
    }

    @RequestMapping(value = "dept/{format:list|chart|pdf|xls|xlsx}")
    public ResponseEntity dept(@PathVariable String format, StatusDeptDto param, Pageable pageable, HttpServletResponse response) {
        switch (format) {
            case LIST:
                return ResponseEntity.ok(mapper.dept(param, pageable));
            case CHART:
                return ResponseEntity.ok(mapper.dept(param, pageable).getContent());
            default:
                return JasperReportsExportHelper.toResponseEntity(response
                        , "jrxml/status-dept.jrxml"
                        , format
                        , mapper.dept(param, pageable).getContent());
        }
    }

    @RequestMapping(value = "major/{format:list|chart|pdf|xls|xlsx}")
    public ResponseEntity major(@PathVariable String format, StatusMajorDto param, Pageable pageable, HttpServletResponse response) {
        switch (format) {
            case LIST:
                return ResponseEntity.ok(mapper.major(param, pageable));
            case CHART:
                return ResponseEntity.ok(mapper.major(param, pageable).getContent());
            default:
                return JasperReportsExportHelper.toResponseEntity(response
                        , "jrxml/status-major.jrxml"
                        , format
                        , mapper.major(param, pageable).getContent());
        }
    }

    @RequestMapping(value = "hall/{format:list|chart|pdf|xls|xlsx}")
    public ResponseEntity hall(@PathVariable String format, StatusHallDto param, Pageable pageable, HttpServletResponse response) {
        switch (format) {
            case LIST:
                return ResponseEntity.ok(mapper.hall(param, pageable));
            case CHART:
                return ResponseEntity.ok(mapper.hall(param, pageable).getContent());
            default:
                return JasperReportsExportHelper.toResponseEntity(response
                        , "jrxml/status-hall.jrxml"
                        , format
                        , mapper.hall(param, pageable).getContent());
        }
    }

    @RequestMapping(value = "group/{format:list|chart|pdf|xls|xlsx}")
    public ResponseEntity group(@PathVariable String format, StatusGroupDto param, Pageable pageable, HttpServletResponse response) {
        switch (format) {
            case LIST:
                return ResponseEntity.ok(mapper.group(param, pageable));
            case CHART:
                return ResponseEntity.ok(mapper.group(param, pageable).getContent());
            default:
                return JasperReportsExportHelper.toResponseEntity(response
                        , "jrxml/status-group.jrxml"
                        , format
                        , mapper.group(param, pageable).getContent());
        }
    }
}