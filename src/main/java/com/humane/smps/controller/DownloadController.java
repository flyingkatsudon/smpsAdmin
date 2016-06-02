package com.humane.smps.controller;

import com.humane.smps.dto.StatusDeptDto;
import com.humane.smps.dto.StatusMajorDto;
import com.humane.smps.mapper.StatusMapper;
import com.humane.util.file.FileNameEncoder;
import com.humane.util.file.FileUtils;
import com.humane.util.jasperreports.JasperReportsExportHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping(value = "download")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DownloadController {
    @Value("${path.image.examinee:C:/api/smps}") String pathRoot;
    private final StatusMapper statusMapper;

    @RequestMapping(value = "item.xlsx", method = RequestMethod.GET)
    public ResponseEntity item() {
        return JasperReportsExportHelper.toResponseEntity(
                "jrxml/upload-item.jrxml",
                "xlsx",
                null
        );
    }

    @RequestMapping(value = "hall.xlsx", method = RequestMethod.GET)
    public ResponseEntity hall() {
        return JasperReportsExportHelper.toResponseEntity(
                "jrxml/upload-hall.jrxml",
                "xlsx",
                null
        );
    }

    @RequestMapping(value = "examinee.xlsx", method = RequestMethod.GET)
    public ResponseEntity examinee() {
        return JasperReportsExportHelper.toResponseEntity(
                "jrxml/upload-examinee.jrxml",
                "xlsx",
                null
        );
    }

    @RequestMapping(value = "devi.xlsx", method = RequestMethod.GET)
    public ResponseEntity devi() {
        return JasperReportsExportHelper.toResponseEntity(
                "jrxml/upload-devi.jrxml",
                "xlsx",
                null
        );
    }

    @RequestMapping(value = "allData.zip", method = RequestMethod.GET)
    public ResponseEntity allData() throws IOException, ZipException {
        Pageable pageable = new PageRequest(0, Integer.MAX_VALUE);

        // 압축파일 생성
        String currentTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        File file = new File(currentTime + "_allData.zip");
        ZipFile zipFile = new ZipFile(file);

        // 압축파일 파라미터 설정
        ZipParameters parameters = new ZipParameters();
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

        // entry 생성
        File fileDept = JasperReportsExportHelper.toFile(
                "jrxml/status-dept.jrxml"
                , "xlsx"
                , statusMapper.dept(new StatusDeptDto(), pageable).getContent());
        zipFile.addFile(fileDept, parameters);
        fileDept.delete();

        File fileMajor = JasperReportsExportHelper.toFile(
                "jrxml/status-major.jrxml"
                , "xlsx"
                , statusMapper.major(new StatusMajorDto(), pageable).getContent());
        zipFile.addFile(fileMajor, parameters);
        fileMajor.delete();

        //
        byte[] ba = FileUtils.getByteArray(zipFile.getFile());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", FileNameEncoder.encode("최종산출물.zip"));
        return new ResponseEntity<>(ba, headers, HttpStatus.OK);
    }
}