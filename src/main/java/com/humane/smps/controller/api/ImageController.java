package com.humane.smps.controller.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@RestController
@RequestMapping(value = "api/image", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
public class ImageController {

    @Value("${path.image.examinee:C:/api/image/examinee}") String pathImageExaminee;

    @RequestMapping(value = "examinee/{fileName:.+}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<InputStreamResource> examinee(@PathVariable("fileName") String fileName) throws FileNotFoundException {
        File file = new File(pathImageExaminee + "/" + fileName);
        if (file.exists()) {
            return ResponseEntity.ok()
                    .contentLength(file.length())
                    .header("Content-Disposition", "attachment; filename=\"" + file.getName() + '"')
                    .body(new InputStreamResource(new FileInputStream(file)));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
}
