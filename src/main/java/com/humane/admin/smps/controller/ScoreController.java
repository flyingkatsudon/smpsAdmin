package com.humane.admin.smps.controller;

import com.humane.admin.smps.dto.StatusDto;
import com.humane.admin.smps.service.ApiService;
import com.humane.util.ObjectConvert;
import com.humane.util.jasperreports.JasperReportsExportHelper;
import com.humane.util.jqgrid.JqgridMapper;
import com.humane.util.jqgrid.JqgridPager;
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

    @RequestMapping(value = "sheet-print/{format:list|pdf|xls|xlsx}")
    public ResponseEntity sheetPrint(@PathVariable String format, StatusDto statusDto, JqgridPager pager, HttpServletResponse response) {
        switch (format) {
            case LIST:
                Response<PageResponse<StatusDto>> pageResponse = apiService.sheet(
                        ObjectConvert.asMap(statusDto),
                        pager.getPage() - 1,
                        pager.getRows(),
                        pager.getSort()
                );
                return ResponseEntity.ok(JqgridMapper.getResponse(pageResponse.body()));
            default:
                return JasperReportsExportHelper.toResponseEntity(response,
                        "jrxml/status-sheet-print.jrxml",
                        format,
                        apiService.sheet(ObjectConvert.asMap(statusDto), pager.getSort())
                );
        }
    }

    @RequestMapping(value = "sheet-cancel/{format:list|pdf|xls|xlsx}")
    public ResponseEntity sheetCancel(@PathVariable String format, StatusDto statusDto, JqgridPager pager, HttpServletResponse response) {
        statusDto.setIsCancel(true);
        switch (format) {
            case LIST:
                Response<PageResponse<StatusDto>> pageResponse = apiService.sheet(
                        ObjectConvert.asMap(statusDto),
                        pager.getPage() - 1,
                        pager.getRows(),
                        pager.getSort()
                );
                return ResponseEntity.ok(JqgridMapper.getResponse(pageResponse.body()));
            default:
                return JasperReportsExportHelper.toResponseEntity(response,
                        "jrxml/status-sheet-cancel.jrxml",
                        format,
                        apiService.sheet(ObjectConvert.asMap(statusDto), pager.getSort())
                );
        }
    }

    @RequestMapping(value = "sheet-log/{format:list|pdf|xls|xlsx}")
    public ResponseEntity sheetLog(@PathVariable String format, StatusDto statusDto, JqgridPager pager, HttpServletResponse response) {
        switch (format) {
            case LIST:
                Response<PageResponse<StatusDto>> pageResponse = apiService.examinee(
                        ObjectConvert.asMap(statusDto),
                        pager.getPage() - 1,
                        pager.getRows(),
                        pager.getSort()
                );
                return ResponseEntity.ok(JqgridMapper.getResponse(pageResponse.body()));
            default:
                return JasperReportsExportHelper.toResponseEntity(response,
                        "jrxml/status-sheet-log.jrxml",
                        format,
                        apiService.examinee(ObjectConvert.asMap(statusDto), pager.getSort())
                );
        }
    }
}