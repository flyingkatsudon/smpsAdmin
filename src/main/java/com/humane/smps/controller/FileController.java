package com.humane.smps.controller;

import com.humane.smps.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "file", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FileController {

    // Windows
    @Value("${path.file.document:C:/api/smps/file/document}")
    // Mac
    //@Value("${path.file.document:/Users/Jeremy/Humane/api/smps/file/document}")
    String basicPath;

    private final FileService fileService;

    /*
        type = {
            {1, "학생부"},
            {2, "자기소개서"},
            {3, "추천서}
        }
     */

    @RequestMapping(value = "document", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> document(@RequestParam(defaultValue = "") String examineeCd, @RequestParam(defaultValue = "") String type){
        return fileService.toResponseEntity(basicPath, examineeCd, type);
    }
}
