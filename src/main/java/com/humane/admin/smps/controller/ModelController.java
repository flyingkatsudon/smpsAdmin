package com.humane.admin.smps.controller;

import com.humane.admin.smps.dto.StatusDto;
import com.humane.admin.smps.service.ApiService;
import com.humane.util.ObjectConvert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import retrofit2.Response;

import java.util.List;

@RestController
@RequestMapping(value = "model", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ModelController {
    private final ApiService apiService;

    /**
     * 툴바 데이터를 전송
     */
    @RequestMapping(value = "toolbar")
    public ResponseEntity getToolbar(StatusDto statusDto) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Response<List<StatusDto>> response = apiService.toolbar(ObjectConvert.asMap(statusDto));
        return ResponseEntity.ok(response.body());
    }
}
