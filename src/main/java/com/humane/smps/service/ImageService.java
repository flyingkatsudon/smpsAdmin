package com.humane.smps.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
@Slf4j
public class ImageService {

    @Value("${path.image.examinee:C:/api/image/examinee}") String pathImageExaminee;

    public InputStream getImageExaminee(String fileName) {
        try {
            File path = new File(pathImageExaminee);
            if (!path.exists()) path.mkdirs();
            File file = new File(pathImageExaminee + "/" + fileName);
            if (file.exists()) {
                return new FileInputStream(file);
            }
        } catch (IOException e) {

        }
        return null;
    }

    public void deleteImageExaminee(String fileName) throws IOException {
        File file = new File("C:/api/image/examinee/" + fileName + ".jpg");
        file.delete();
    }
}
