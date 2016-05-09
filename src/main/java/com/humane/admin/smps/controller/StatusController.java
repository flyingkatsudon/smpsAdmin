package com.humane.admin.smps.controller;

import com.humane.admin.smps.dto.StatusDto;
import com.humane.admin.smps.service.ApiService;
import com.humane.util.ObjectConvert;
import com.humane.util.jasperreports.JasperReportsExportHelper;
import com.humane.util.jqgrid.JqgridMapper;
import com.humane.util.jqgrid.JqgridPager;
import com.humane.util.spring.PageResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import retrofit2.Response;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "status")
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class StatusController {
    private static final String CHART = "chart";
    private static final String LIST = "list";

    private final ApiService apiService;

    @RequestMapping(value = "all/{format:chart}")
    public ResponseEntity all(@PathVariable String format, StatusDto statusDto, JqgridPager pager) {
        Response<StatusDto> response = apiService.all(ObjectConvert.asMap(statusDto), pager.getSort());
        return ResponseEntity.ok(response.body());
    }

    @RequestMapping(value = "attend/{format:list|chart|pdf|xls|xlsx}")
    public ResponseEntity attend(@PathVariable String format, StatusDto statusDto, JqgridPager pager, HttpServletResponse response) {

        switch (format) {
            case LIST:
                Response<PageResponse<StatusDto>> pageResponse = apiService.attend(
                        ObjectConvert.asMap(statusDto),
                        pager.getPage() - 1,
                        pager.getRows(),
                        pager.getSort()
                );
                return ResponseEntity.ok(JqgridMapper.getResponse(pageResponse.body()));
            case CHART:
                return ResponseEntity.ok(apiService.attend(ObjectConvert.asMap(statusDto), pager.getSort()));
            default:
                return JasperReportsExportHelper.toResponseEntity(response,
                        "jrxml/status-attend.jrxml",
                        format,
                        apiService.attend(ObjectConvert.asMap(statusDto), pager.getSort())
                );
        }
    }

    @RequestMapping(value = "dept/{format:list|chart|pdf|xls|xlsx}")
    public ResponseEntity dept(@PathVariable String format, StatusDto statusDto, JqgridPager pager, HttpServletResponse response) {

        switch (format) {
            case LIST:
                Response<PageResponse<StatusDto>> pageResponse = apiService.dept(
                        ObjectConvert.asMap(statusDto),
                        pager.getPage() - 1,
                        pager.getRows(),
                        pager.getSort()
                );
                return ResponseEntity.ok(JqgridMapper.getResponse(pageResponse.body()));
            case CHART:
                return ResponseEntity.ok(apiService.dept(ObjectConvert.asMap(statusDto), pager.getSort()));
            default:
                return JasperReportsExportHelper.toResponseEntity(response,
                        "jrxml/status-dept.jrxml",
                        format,
                        apiService.dept(ObjectConvert.asMap(statusDto), pager.getSort())
                );
        }
    }

    @RequestMapping(value = "hall/{format:list|chart|pdf|xls|xlsx}")
    public ResponseEntity hall(@PathVariable String format, StatusDto statusDto, JqgridPager pager, HttpServletResponse response) {
        switch (format) {
            case LIST:
                Response<PageResponse<StatusDto>> pageResponse = apiService.hall(
                        ObjectConvert.asMap(statusDto),
                        pager.getPage() - 1,
                        pager.getRows(),
                        pager.getSort()
                );
                return ResponseEntity.ok(JqgridMapper.getResponse(pageResponse.body()));
            case CHART:
                return ResponseEntity.ok(apiService.hall(ObjectConvert.asMap(statusDto), pager.getSort()));
            default:
                return JasperReportsExportHelper.toResponseEntity(response,
                        "jrxml/status-hall.jrxml",
                        format,
                        apiService.hall(ObjectConvert.asMap(statusDto), pager.getSort())
                );
        }
    }

    @RequestMapping(value = "group/{format:list|chart|pdf|xls|xlsx}")
    public ResponseEntity group(@PathVariable String format, StatusDto statusDto, JqgridPager pager, HttpServletResponse response) {
        switch (format) {
            case LIST:
                Response<PageResponse<StatusDto>> pageResponse = apiService.group(
                        ObjectConvert.asMap(statusDto),
                        pager.getPage() - 1,
                        pager.getRows(),
                        pager.getSort()
                );
                return ResponseEntity.ok(JqgridMapper.getResponse(pageResponse.body()));
            case CHART:
                return ResponseEntity.ok(apiService.group(ObjectConvert.asMap(statusDto), pager.getSort()));
            default:
                return JasperReportsExportHelper.toResponseEntity(response,
                        "jrxml/status-hall.jrxml",
                        format,
                        apiService.group(ObjectConvert.asMap(statusDto), pager.getSort())
                );
        }
    }
}