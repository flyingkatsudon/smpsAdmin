package com.humane.smps.controller.admin;

import com.humane.smps.dto.DownloadWrapper;
import com.humane.smps.service.ApiService;
import com.humane.smps.service.SystemService;
import com.humane.util.retrofit.ServiceBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping(value = "system", method = RequestMethod.GET)
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SystemController {
    private final SystemService systemService;

    @RequestMapping(value = "server.json", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity listServer() {
        ApiService apiService = ServiceBuilder.INSTANCE.createService("http://humane.ipdisk.co.kr:10000", ApiService.class);
        try {
            return ResponseEntity.ok(apiService.checkUrl().toBlocking().first());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "examHall.json", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity listExamHall(
            @RequestParam(required = false, defaultValue = "http://humane.ipdisk.co.kr:9000") String url,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "") String sort) {
        ApiService apiService = ServiceBuilder.INSTANCE.createService(url, ApiService.class);
        try {
            return ResponseEntity.ok(apiService.examHall(new HashMap<>(), page, size, sort).toBlocking().first());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "download", method = RequestMethod.POST)
    public ResponseEntity download(@RequestBody DownloadWrapper wrapper) {
        // 1. validate wrapper
        if (StringUtils.isEmpty(wrapper.getUrl()))
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("url이 올바르지 않습니다.");

        for (DownloadWrapper.ExamHallWrapper examHall : wrapper.getList()) {
            if (StringUtils.isEmpty(examHall.getExamCd()) || StringUtils.isEmpty(examHall.getHallCd()))
                return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("시험코드 및 고사실이 올바르지 않습니다.");
        }

        // 2. create http service
        ApiService apiService = ServiceBuilder.INSTANCE.createService(wrapper.getUrl(), ApiService.class);

        // 3. getData(iterator)

        systemService.saveExamHall(apiService, wrapper);

        Set<String> examCdSet = new HashSet<>();
        wrapper.getList().forEach(examHallWrapper -> examCdSet.add(examHallWrapper.getExamCd()));

        systemService.saveItem(apiService, examCdSet);
        systemService.saveDevi(apiService);
        return null;
    }

    @RequestMapping(value = "reset")
    public void reset() {
        systemService.resetData();
    }

    @RequestMapping(value = "init")
    public void init() {
        systemService.initData();
    }
}