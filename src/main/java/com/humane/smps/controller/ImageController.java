package com.humane.smps.controller;

import com.humane.smps.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;

@RestController
@RequestMapping(value = "image", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ImageController {
    private final ImageService imageService;

    @RequestMapping(value = "examinee/{fileName:.+}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<InputStreamResource> examinee(@PathVariable("fileName") String fileName) {
        InputStream inputStream = imageService.getExaminee(fileName);
        return ResponseEntity.ok(new InputStreamResource(inputStream));
    }

    @RequestMapping(value = "univLogo", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<InputStreamResource> univLogo() {
        InputStream inputStream = imageService.getUnivLogo("univLogo.png");
        return ResponseEntity.ok(new InputStreamResource(inputStream));
    }
}
