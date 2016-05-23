package com.humane.smps.controller.admin;

import com.humane.smps.dto.ExamineeDto;
import com.humane.smps.dto.ScoreDto;
import com.humane.smps.mapper.DataMapper;
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
@RequestMapping(value = "data", method = RequestMethod.GET)
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DataController {
    private static final String CHART = "chart";
    private static final String LIST = "list";
    private final DataMapper mapper;

    @RequestMapping(value = "examinee/{format:list|chart|pdf|xls|xlsx}")
    public ResponseEntity examinee(@PathVariable String format, ExamineeDto param, Pageable pageable, HttpServletResponse response) {
        switch (format) {
            case LIST:
                return ResponseEntity.ok(mapper.examinee(param, pageable));
            case CHART:
                return ResponseEntity.ok(mapper.examinee(param, pageable).getContent());
            default:
                return JasperReportsExportHelper.toResponseEntity(response
                        , "jrxml/data-examinee.jrxml"
                        , format
                        , mapper.examinee(param, pageable).getContent());
        }
    }

    @RequestMapping(value = "scorer/{format:list|chart|pdf|xls|xlsx}")
    public ResponseEntity scorer(@PathVariable String format, ScoreDto param, Pageable pageable, HttpServletResponse response) {
        switch (format) {
            case LIST:
                return ResponseEntity.ok(mapper.score(param, pageable));
            case CHART:
                return ResponseEntity.ok(mapper.score(param, pageable).getContent());
            default:
                return JasperReportsExportHelper.toResponseEntity(response
                        , "jrxml/data-scorer.jrxml"
                        , format
                        , mapper.score(param, pageable).getContent());
        }
    }
}