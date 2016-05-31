package com.humane.smps.controller.admin;

import com.humane.smps.dto.ScoreFixDto;
import com.humane.smps.dto.SheetDto;
import com.humane.smps.mapper.AdminScoreMapper;
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
@RequestMapping(value = "score", method = RequestMethod.GET)
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdminScoreController {
    private static final String CHART = "chart";
    private static final String JSON = "json";
    private final AdminScoreMapper mapper;

    @RequestMapping(value = "print.{format:json|chart|pdf|xls|xlsx}")
    public ResponseEntity print(@PathVariable String format, SheetDto param, Pageable pageable) {
        switch (format) {
            case JSON:
                return ResponseEntity.ok(mapper.sheet(param, pageable));
            case CHART:
                return ResponseEntity.ok(mapper.sheet(param, pageable).getContent());
            default:
                return JasperReportsExportHelper.toResponseEntity(
                        "jrxml/score-print.jrxml"
                        , format
                        , mapper.sheet(param, pageable).getContent());
        }
    }

    @RequestMapping(value = "cancel.{format:json|chart|pdf|xls|xlsx}")
    public ResponseEntity cancel(@PathVariable String format, SheetDto param, Pageable pageable) {
        param.setIsCancel(true);
        switch (format) {
            case JSON:
                return ResponseEntity.ok(mapper.sheet(param, pageable));
            case CHART:
                return ResponseEntity.ok(mapper.sheet(param, pageable).getContent());
            default:
                return JasperReportsExportHelper.toResponseEntity(
                        "jrxml/score-cancel.jrxml"
                        , format
                        , mapper.sheet(param, pageable).getContent());
        }
    }

    @RequestMapping(value = "fix.{format:json|chart|pdf|xls|xlsx}")
    public ResponseEntity fix(@PathVariable String format, ScoreFixDto param, Pageable pageable) {
        switch (format) {
            case JSON:
                return ResponseEntity.ok(mapper.fix(param, pageable));
            case CHART:
                return ResponseEntity.ok(mapper.fix(param, pageable).getContent());
            default:
                return JasperReportsExportHelper.toResponseEntity(
                        "jrxml/score-fix.jrxml"
                        , format
                        , mapper.fix(param, pageable).getContent());
        }
    }
}