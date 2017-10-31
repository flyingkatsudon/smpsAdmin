package com.humane.smps.controller.admin;

import com.humane.smps.dto.*;
import com.humane.smps.mapper.StatusMapper;
import com.humane.util.jasperreports.JasperReportsExportHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "status")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StatusController {
    private static final String JSON = "json";
    private final StatusMapper statusMapper;

    /*
     * '응시율 통계'
     * all(StatusDto param): 통계 요약
     * 그 외: '모집단위별', '전공별', 고사실별', '조별' 통계
     *
     * JasperReportsExportHelper
     * 1. 'xlsx' 파일로 파일 생성
     * 2. '.jrxml' 타입의 양식파일에 데이터를 매칭시켜 xlsx 파일로 만듦
     */

    @RequestMapping(value = "all")
    public ResponseEntity all(StatusDto param) {
        log.debug("{}", param);
        return ResponseEntity.ok(statusMapper.all(param));
    }

    @RequestMapping(value = "dept.{format:json|pdf|xls|xlsx}")
    public ResponseEntity dept(@PathVariable String format, StatusDto param, Pageable pageable) {
        switch (format) {
            case JSON:
                return ResponseEntity.ok(statusMapper.dept(param, pageable));
            default:
                return JasperReportsExportHelper.toResponseEntity(
                        "jrxml/status-dept.jrxml"
                        , format
                        , statusMapper.dept(param, new PageRequest(0, Integer.MAX_VALUE, pageable.getSort())).getContent());
        }
    }

    @RequestMapping(value = "major.{format:json|pdf|xls|xlsx}")
    public ResponseEntity major(@PathVariable String format, StatusDto param, Pageable pageable) {
        switch (format) {
            case JSON:
                return ResponseEntity.ok(statusMapper.major(param, pageable));
            default:
                return JasperReportsExportHelper.toResponseEntity(
                        "jrxml/status-major.jrxml"
                        , format
                        , statusMapper.major(param, new PageRequest(0, Integer.MAX_VALUE, pageable.getSort())).getContent());
        }
    }

    @RequestMapping(value = "hall.{format:json|pdf|xls|xlsx}")
    public ResponseEntity hall(@PathVariable String format, StatusDto param, Pageable pageable) {
        switch (format) {
            case JSON:
                return ResponseEntity.ok(statusMapper.hall(param, pageable));
            default:
                return JasperReportsExportHelper.toResponseEntity(
                        "jrxml/status-hall.jrxml"
                        , format
                        , statusMapper.hall(param, new PageRequest(0, Integer.MAX_VALUE, pageable.getSort())).getContent());
        }
    }

    @RequestMapping(value = "group.{format:json|pdf|xls|xlsx}")
    public ResponseEntity group(@PathVariable String format, StatusDto param, Pageable pageable) {
        switch (format) {
            case JSON:
                return ResponseEntity.ok(statusMapper.group(param, pageable));
            default:
                return JasperReportsExportHelper.toResponseEntity(
                        "jrxml/status-group.jrxml"
                        , format
                        , statusMapper.group(param, new PageRequest(0, Integer.MAX_VALUE, pageable.getSort())).getContent());
        }
    }
}