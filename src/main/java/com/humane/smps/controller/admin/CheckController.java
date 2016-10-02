package com.humane.smps.controller.admin;

import com.humane.smps.dto.CheckItemDto;
import com.humane.smps.dto.CheckScorerDto;
import com.humane.smps.dto.CheckSendDto;
import com.humane.smps.mapper.CheckMapper;
import com.humane.util.jasperreports.JasperReportsExportHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "check", method = RequestMethod.GET)
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CheckController {
    private static final String JSON = "json";
    private final CheckMapper mapper;

    @RequestMapping(value = "send.{format:json|pdf|xls|xlsx}")
    public ResponseEntity send(@PathVariable String format, CheckSendDto param, Pageable pageable) {
        switch (format) {
            case JSON:
                return ResponseEntity.ok(mapper.send(param, pageable));
            default:
                return JasperReportsExportHelper.toResponseEntity(
                        "jrxml/check-send.jrxml"
                        , format
                        , mapper.send(param, new PageRequest(0, Integer.MAX_VALUE, pageable.getSort())).getContent());
        }
    }

    @RequestMapping(value = "item.{format:json|pdf|xls|xlsx}")
    public ResponseEntity item(@PathVariable String format, CheckItemDto param, Pageable pageable) {
        switch (format) {
            case JSON:
                return ResponseEntity.ok(mapper.item(param, pageable));
            default:
                return JasperReportsExportHelper.toResponseEntity(
                        "jrxml/check-item.jrxml"
                        , format
                        , mapper.item(param, new PageRequest(0, Integer.MAX_VALUE, pageable.getSort())).getContent());
        }
    }

    @RequestMapping(value = "scorer.{format:json|pdf|xls|xlsx}")
    public ResponseEntity scorer(@PathVariable String format, CheckScorerDto param, Pageable pageable) {
        switch (format) {
            case JSON:
                return ResponseEntity.ok(mapper.scorer(param, pageable));
            default:
                return JasperReportsExportHelper.toResponseEntity(
                        "jrxml/check-scorer.jrxml"
                        , format
                        , mapper.scorer(param, new PageRequest(0, Integer.MAX_VALUE, pageable.getSort())).getContent());
        }
    }
}