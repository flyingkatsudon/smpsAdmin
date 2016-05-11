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
@RequestMapping(value = "check")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CheckController {
    private final ApiService apiService;
    private static final String LIST = "list";

    @RequestMapping(value = "send/{format:list|pdf|xls|xlsx}")
    public ResponseEntity send(@PathVariable String format, StatusDto statusDto, JqgridPager pager, HttpServletResponse response) {
        switch (format) {
            case LIST:
                Response<PageResponse<StatusDto>> pageResponse = apiService.send(
                        ObjectConvert.asMap(statusDto),
                        pager.getPage() - 1,
                        pager.getRows(),
                        pager.getSort()
                );
                return ResponseEntity.ok(JqgridMapper.getResponse(pageResponse.body()));
            default:
                return JasperReportsExportHelper.toResponseEntity(response,
                        "jrxml/check-send.jrxml",
                        format,
                        apiService.send(ObjectConvert.asMap(statusDto), pager.getSort())
                );
        }
    }
    @RequestMapping(value = "virtNo/{format:list|pdf|xls|xlsx}")
    public ResponseEntity virtNo(@PathVariable String format, StatusDto statusDto, JqgridPager pager, HttpServletResponse response) {
        switch (format) {
            case LIST:
                Response<PageResponse<StatusDto>> pageResponse = apiService.virtNo(
                        ObjectConvert.asMap(statusDto),
                        pager.getPage() - 1,
                        pager.getRows(),
                        pager.getSort()
                );
                return ResponseEntity.ok(JqgridMapper.getResponse(pageResponse.body()));
            default:
                return JasperReportsExportHelper.toResponseEntity(response,
                        "jrxml/check-virtNo.jrxml",
                        format,
                        apiService.virtNo(ObjectConvert.asMap(statusDto), pager.getSort())
                );
        }
    }
    @RequestMapping(value = "scoring/{format:list|pdf|xls|xlsx}")
    public ResponseEntity scoring(@PathVariable String format, StatusDto statusDto, JqgridPager pager, HttpServletResponse response) {
        switch (format) {
            case LIST:
                Response<PageResponse<StatusDto>> pageResponse = apiService.scoring(
                        ObjectConvert.asMap(statusDto),
                        pager.getPage() - 1,
                        pager.getRows(),
                        pager.getSort()
                );
                return ResponseEntity.ok(JqgridMapper.getResponse(pageResponse.body()));
            default:
                return JasperReportsExportHelper.toResponseEntity(response,
                        "jrxml/check-scoring.jrxml",
                        format,
                        apiService.scoring(ObjectConvert.asMap(statusDto), pager.getSort())
                );
        }
    }
}