package com.humane.admin.smps.controller;

import com.humane.admin.smps.dto.CheckItemDto;
import com.humane.admin.smps.dto.CheckScorerDto;
import com.humane.admin.smps.dto.CheckSendDto;
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
    public ResponseEntity send(@PathVariable String format, CheckSendDto checkSendDto, JqgridPager pager, HttpServletResponse response) {
        switch (format) {
            case LIST:
                Response<PageResponse<CheckSendDto>> pageResponse = apiService.send(
                        ObjectConvert.asMap(checkSendDto),
                        pager.getPage() - 1,
                        pager.getRows(),
                        pager.getSort()
                );
                return ResponseEntity.ok(JqgridMapper.getResponse(pageResponse.body()));
            default:
                return JasperReportsExportHelper.toResponseEntity(response,
                        "jrxml/check-send.jrxml",
                        format,
                        apiService.send(ObjectConvert.asMap(checkSendDto), pager.getSort())
                );
        }
    }
    @RequestMapping(value = "checkItem/{format:list|pdf|xls|xlsx}")
    public ResponseEntity checkItem(@PathVariable String format, CheckItemDto checkItemDto, JqgridPager pager, HttpServletResponse response) {
        switch (format) {
            case LIST:
                Response<PageResponse<CheckItemDto>> pageResponse = apiService.checkItem(
                        ObjectConvert.asMap(checkItemDto),
                        pager.getPage() - 1,
                        pager.getRows(),
                        pager.getSort()
                );
                return ResponseEntity.ok(JqgridMapper.getResponse(pageResponse.body()));
            default:
                return JasperReportsExportHelper.toResponseEntity(response,
                        "jrxml/check-checkItem.jrxml",
                        format,
                        apiService.checkItem(ObjectConvert.asMap(checkItemDto), pager.getSort())
                );
        }
    }
    @RequestMapping(value = "checkScorer/{format:list|pdf|xls|xlsx}")
    public ResponseEntity checkScorer(@PathVariable String format, CheckScorerDto checkScorerDto, JqgridPager pager, HttpServletResponse response) {
        switch (format) {
            case LIST:
                Response<PageResponse<CheckScorerDto>> pageResponse = apiService.checkScorer(
                        ObjectConvert.asMap(checkScorerDto),
                        pager.getPage() - 1,
                        pager.getRows(),
                        pager.getSort()
                );
                return ResponseEntity.ok(JqgridMapper.getResponse(pageResponse.body()));
            default:
                return JasperReportsExportHelper.toResponseEntity(response,
                        "jrxml/check-checkScorer.jrxml",
                        format,
                        apiService.checkScorer(ObjectConvert.asMap(checkScorerDto), pager.getSort())
                );
        }
    }
}