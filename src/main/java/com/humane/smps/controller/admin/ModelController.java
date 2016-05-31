package com.humane.smps.controller.admin;

import com.humane.smps.dto.StatusDto;
import com.humane.smps.mapper.ModelMapper;
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

@RestController
@RequestMapping(value = "model", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ModelController {
    private final ModelMapper mapper;

    /**
     * 툴바 데이터를 전송
     */
    @RequestMapping(value = "toolbar.json")
    public ResponseEntity toolbar(StatusDto statusDto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return ResponseEntity.ok(mapper.toolbar(statusDto));
    }
}
