package com.humane.smps.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
@Slf4j
public class FileService {
    public ResponseEntity<InputStreamResource> toResponseEntity(String basicPath, String examineeCd, String type) {

        String fileName = examineeCd + ".pdf";

        try {
            InputStream inputStream = getFile(basicPath, fileName, type);
            return ResponseEntity.ok(new InputStreamResource(inputStream));
        } catch (Exception e) {
            log.error("{}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    private InputStream getFile(String basicPath, String fileName, String type) throws FileNotFoundException {
        try {
            String filePath = basicPath + "/" + type;

            File path = new File(filePath);
            if (!path.exists()) path.mkdirs();

            File file = new File(path, fileName);
            if (file.exists()) {
                return new FileInputStream(file);
            }
        } catch (IOException ignored) {

        }
        return null;
    }

    public void deleteFiles(String... path) {
        for (String p : path) {
            File folder = new File(p);
            File[] files = folder.listFiles();
            if (files != null)
                for (File file : files) {
                    file.delete();
                }
        }
    }
}
