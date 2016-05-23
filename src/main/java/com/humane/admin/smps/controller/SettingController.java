package com.humane.admin.smps.controller;

import com.blogspot.na5cent.exom.ExOM;
import com.humane.admin.smps.dto.UploadExamineeDto;
import com.humane.admin.smps.dto.UploadHallDto;
import com.humane.admin.smps.dto.UploadItemDto;
import com.humane.util.jasperreports.JasperReportsExportHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(value = "setting")
@Slf4j
public class SettingController {

    @RequestMapping(value = "uploadItem")
    public void uploadItem(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        File tempFile = File.createTempFile("test", ".tmp");
        multipartFile.transferTo(tempFile);

        try {
            List<UploadItemDto> itemList = ExOM.mapFromExcel(tempFile).to(UploadItemDto.class).map(1);
            itemList.forEach(uploadItemDto -> log.debug("{}", uploadItemDto));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            tempFile.delete();
        }
    }

    @RequestMapping(value = "uploadHall")
    public void uploadHall(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        File tempFile = File.createTempFile("test", ".tmp");
        multipartFile.transferTo(tempFile);

        try {
            List<UploadHallDto> hallList = ExOM.mapFromExcel(tempFile).to(UploadHallDto.class).map(1);
            hallList.forEach(uploadHallDto -> log.debug("{}", uploadHallDto));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            tempFile.delete();
        }
    }

    @RequestMapping(value = "uploadExaminee")
    public void uploadExaminee(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        File tempFile = File.createTempFile("test", ".tmp");
        multipartFile.transferTo(tempFile);

        try {
            List<UploadExamineeDto> examineeList = ExOM.mapFromExcel(tempFile).to(UploadExamineeDto.class).map(1);
            examineeList.forEach(uploadExamineeDto -> log.debug("{}", uploadExamineeDto));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            tempFile.delete();
        }
    }

    @RequestMapping(value = "downloadItem")
    public ResponseEntity downloadItem(HttpServletResponse response) {
        return JasperReportsExportHelper.toResponseEntity(response,
                "jrxml/setting-item.jrxml",
                "xlsx",
                Collections.emptyList()
        );
    }

    @RequestMapping(value = "downloadHall")
    public ResponseEntity downloadHall(HttpServletResponse response){
        return JasperReportsExportHelper.toResponseEntity(response,
                "jrxml/setting-hall.jrxml",
                "xlsx",
                Collections.emptyList()
        );
    }

    @RequestMapping(value = "downloadExaminee")
    public ResponseEntity downloadExaminee(HttpServletResponse response){
        return JasperReportsExportHelper.toResponseEntity(response,
                "jrxml/setting-examinee.jrxml",
                "xlsx",
                Collections.emptyList()
        );
    }
}