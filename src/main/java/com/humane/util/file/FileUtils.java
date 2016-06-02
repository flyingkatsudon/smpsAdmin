package com.humane.util.file;

import org.apache.commons.io.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtils {
    public static File saveFile(String path, MultipartFile multipartFile) throws IOException {
        return saveFile(new File(path), multipartFile);
    }

    public static File saveFile(File path, MultipartFile multipartFile) throws IOException {
        if (!path.exists()) path.mkdirs();

        String currentTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        File file = new File(path, currentTime + "_" + multipartFile.getOriginalFilename());
        multipartFile.transferTo(file);
        return file;
    }

    public static byte[] getByteArray(File file) {
        // 압축파일 내보내기
        try (FileInputStream fis = new FileInputStream(file)) {
            return IOUtils.toByteArray(fis);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            file.delete();
        }
        return null;
    }
}
