package com.humane.smps.controller.admin;

import com.humane.smps.dto.CheckItemDto;
import com.humane.smps.dto.CheckScorerDto;
import com.humane.smps.dto.SendDto;
import com.humane.smps.mapper.CheckMapper;
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
@RequestMapping(value = "check", method = RequestMethod.GET)
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CheckController {
    private static final String CHART = "chart";
    private static final String LIST = "list";
    private final CheckMapper mapper;

    @RequestMapping(value = "send/{format:list|chart|pdf|xls|xlsx}")
    public ResponseEntity send(@PathVariable String format, SendDto param, Pageable pageable, HttpServletResponse response) {
        switch (format) {
            case LIST:
                return ResponseEntity.ok(mapper.send(param, pageable));
            case CHART:
                return ResponseEntity.ok(mapper.send(param, pageable).getContent());
            default:
                return JasperReportsExportHelper.toResponseEntity(response
                        , "jrxml/check-send.jrxml"
                        , format
                        , mapper.send(param, pageable).getContent());
        }
    }

    @RequestMapping(value = "item/{format:list|chart|pdf|xls|xlsx}")
    public ResponseEntity item(@PathVariable String format, CheckItemDto param, Pageable pageable, HttpServletResponse response) {
        switch (format) {
            case LIST:
                return ResponseEntity.ok(mapper.item(param, pageable));
            case CHART:
                return ResponseEntity.ok(mapper.item(param, pageable).getContent());
            default:
                return JasperReportsExportHelper.toResponseEntity(response
                        , "jrxml/check-item.jrxml"
                        , format
                        , mapper.item(param, pageable).getContent());
        }
    }

    @RequestMapping(value = "scorer/{format:list|chart|pdf|xls|xlsx}")
    public ResponseEntity scorer(@PathVariable String format, CheckScorerDto param, Pageable pageable, HttpServletResponse response) {
        switch (format) {
            case LIST:
                return ResponseEntity.ok(mapper.scorer(param, pageable));
            case CHART:
                return ResponseEntity.ok(mapper.scorer(param, pageable).getContent());
            default:
                return JasperReportsExportHelper.toResponseEntity(response
                        , "jrxml/check-scorer.jrxml"
                        , format
                        , mapper.scorer(param, pageable).getContent());
        }
    }
}