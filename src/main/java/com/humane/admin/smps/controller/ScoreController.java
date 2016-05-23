package com.humane.admin.smps.controller;

import com.humane.admin.smps.dto.ScoreFixDto;
import com.humane.admin.smps.dto.SheetDto;
import com.humane.admin.smps.service.ApiService;
import com.humane.util.ObjectConvert;
import com.humane.util.jasperreports.JasperReportsExportHelper;
import com.humane.util.spring.PageRequest;
import com.humane.util.spring.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import retrofit2.Response;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "score")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ScoreController {
    private final ApiService apiService;
    private static final String LIST = "list";

    @RequestMapping(value = "print/{format:list|pdf|xls|xlsx}")
    public ResponseEntity print(@PathVariable String format, SheetDto sheetDto, PageRequest pager, HttpServletResponse response) {
        switch (format) {
            case LIST:
                Response<PageResponse<SheetDto>> pageResponse = apiService.sheet(
                        ObjectConvert.asMap(sheetDto),
                        pager.getPage(),
                        pager.getSize(),
                        pager.getSort()
                );
                return ResponseEntity.ok(pageResponse.body());
            default:
                return JasperReportsExportHelper.toResponseEntity(response,
                        "jrxml/score-print.jrxml",
                        format,
                        apiService.sheet(ObjectConvert.asMap(sheetDto), pager.getSort())
                );
        }
    }

    @RequestMapping(value = "cancel/{format:list|pdf|xls|xlsx}")
    public ResponseEntity cancel(@PathVariable String format, SheetDto sheetDto, PageRequest pager, HttpServletResponse response) {
        sheetDto.setIsCancel(true);
        switch (format) {
            case LIST:
                Response<PageResponse<SheetDto>> pageResponse = apiService.sheet(
                        ObjectConvert.asMap(sheetDto),
                        pager.getPage(),
                        pager.getSize(),
                        pager.getSort()
                );
                return ResponseEntity.ok(pageResponse.body());
            default:
                return JasperReportsExportHelper.toResponseEntity(response,
                        "jrxml/score-cancel.jrxml",
                        format,
                        apiService.sheet(ObjectConvert.asMap(sheetDto), pager.getSort())
                );
        }
    }

    @RequestMapping(value = "scoreFix/{format:list|pdf|xls|xlsx}")
    public ResponseEntity scoreFix(@PathVariable String format, ScoreFixDto scoreFixDto, PageRequest pager, HttpServletResponse response) {
        switch (format) {
            case LIST:
                Response<PageResponse<ScoreFixDto>> pageResponse = apiService.scoreFix(
                        ObjectConvert.asMap(scoreFixDto),
                        pager.getPage(),
                        pager.getSize(),
                        pager.getSort()
                );
                return ResponseEntity.ok(pageResponse.body());
            default:
                return JasperReportsExportHelper.toResponseEntity(response,
                        "jrxml/score-scoreFix.jrxml",
                        format,
                        apiService.scoreFix(ObjectConvert.asMap(scoreFixDto), pager.getSort())
                );
        }
    }
}