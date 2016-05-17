package com.humane.admin.smps.controller;

import com.humane.admin.smps.dto.*;
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

    @RequestMapping(value = "dept/{format:list|chart|pdf|xls|xlsx}")
    public ResponseEntity attendDept(@PathVariable String format, StatusDeptDto statusDeptDto, JqgridPager pager, HttpServletResponse response) {
        switch (format) {
            case LIST:
                Response<PageResponse<StatusDeptDto>> pageResponse = apiService.attendDept(
                        ObjectConvert.asMap(statusDeptDto),
                        pager.getPage() - 1,
                        pager.getRows(),
                        pager.getSort()
                );
                return ResponseEntity.ok(JqgridMapper.getResponse(pageResponse.body()));
            case CHART:
                return ResponseEntity.ok(apiService.attendDept(ObjectConvert.asMap(statusDeptDto), pager.getSort()));
            default:
                return JasperReportsExportHelper.toResponseEntity(response,
                        "jrxml/status-dept.jrxml",
                        format,
                        apiService.attendDept(ObjectConvert.asMap(statusDeptDto), pager.getSort())
                );
        }
    }

    @RequestMapping(value = "major/{format:list|chart|pdf|xls|xlsx}")
    public ResponseEntity attendMajor(@PathVariable String format, StatusMajorDto stautsMajorDto, JqgridPager pager, HttpServletResponse response) {
        switch (format) {
            case LIST:
                Response<PageResponse<StatusMajorDto>> pageResponse = apiService.attendMajor(
                        ObjectConvert.asMap(stautsMajorDto),
                        pager.getPage() - 1,
                        pager.getRows(),
                        pager.getSort()
                );
                return ResponseEntity.ok(JqgridMapper.getResponse(pageResponse.body()));
            case CHART:
                return ResponseEntity.ok(apiService.attendMajor(ObjectConvert.asMap(stautsMajorDto), pager.getSort()));
            default:
                return JasperReportsExportHelper.toResponseEntity(response,
                        "jrxml/status-major.jrxml",
                        format,
                        apiService.attendMajor(ObjectConvert.asMap(stautsMajorDto), pager.getSort())
                );
        }
    }

    @RequestMapping(value = "hall/{format:list|chart|pdf|xls|xlsx}")
    public ResponseEntity attendHall(@PathVariable String format, StatusHallDto statusHallDto, JqgridPager pager, HttpServletResponse response) {
        switch (format) {
            case LIST:
                Response<PageResponse<StatusHallDto>> pageResponse = apiService.attendHall(
                        ObjectConvert.asMap(statusHallDto),
                        pager.getPage() - 1,
                        pager.getRows(),
                        pager.getSort()
                );
                return ResponseEntity.ok(JqgridMapper.getResponse(pageResponse.body()));
            case CHART:
                return ResponseEntity.ok(apiService.attendHall(ObjectConvert.asMap(statusHallDto), pager.getSort()));
            default:
                return JasperReportsExportHelper.toResponseEntity(response,
                        "jrxml/status-hall.jrxml",
                        format,
                        apiService.attendHall(ObjectConvert.asMap(statusHallDto), pager.getSort())
                );
        }
    }
    
    @RequestMapping(value = "group/{format:list|chart|pdf|xls|xlsx}")
    public ResponseEntity attendGroup(@PathVariable String format, StatusGroupDto statusGroupDto, JqgridPager pager, HttpServletResponse response){
        switch (format) {
            case LIST:
                Response<PageResponse<StatusGroupDto>> pageResponse = apiService.attendGroup(
                        ObjectConvert.asMap(statusGroupDto),
                        pager.getPage() - 1,
                        pager.getRows(),
                        pager.getSort()
                );
                return ResponseEntity.ok(JqgridMapper.getResponse(pageResponse.body()));
            case CHART:
                return ResponseEntity.ok(apiService.attendGroup(ObjectConvert.asMap(statusGroupDto), pager.getSort()));
            default:
                return JasperReportsExportHelper.toResponseEntity(response,
                        "jrxml/status-hall.jrxml",
                        format,
                        apiService.attendGroup(ObjectConvert.asMap(statusGroupDto), pager.getSort())
                );
        }
    }
}