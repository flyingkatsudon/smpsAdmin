package com.humane.smps.controller;

import com.humane.smps.dto.*;
import com.humane.smps.mapper.AdminScoreMapper;
import com.humane.smps.mapper.CheckMapper;
import com.humane.smps.mapper.DataMapper;
import com.humane.smps.mapper.StatusMapper;
import com.humane.util.file.FileNameEncoder;
import com.humane.util.file.FileUtils;
import com.humane.util.jasperreports.JasperReportsExportHelper;
import com.humane.util.zip4j.ZipFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.exception.ZipException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    private final DataMapper dataMapper;
    private final CheckMapper checkMapper;
    private final AdminScoreMapper adminScoreMapper;

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

        // entry 생성
        File fileDept = JasperReportsExportHelper.toXlsxFile(
                "jrxml/status-dept.jrxml"
                , statusMapper.dept(new StatusDeptDto(), pageable).getContent());
        zipFile.addFile(fileDept);
        fileDept.delete();

        File fileMajor = JasperReportsExportHelper.toXlsxFile(
                "jrxml/status-major.jrxml"
                , statusMapper.major(new StatusMajorDto(), pageable).getContent());
        zipFile.addFile(fileMajor);
        fileMajor.delete();

        //1. xlsx 파일 생성
        File fileHall = JasperReportsExportHelper.toXlsxFile(
                "jrxml/status-hall.jrxml"
                , statusMapper.hall(new StatusHallDto(), pageable).getContent());
        //2. zip파일에 추가시키기
        zipFile.addFile(fileHall);
        //3. 추가시킨 후 xlsx파일 삭제
        fileHall.delete();

        // 1. xlsx 파일 생성
        File fileGroup = JasperReportsExportHelper.toXlsxFile(
                "jrxml/status-group.jrxml"
                , statusMapper.group(new StatusGroupDto(), pageable).getContent()
        );
        zipFile.addFile(fileGroup);
        fileGroup.delete();

        File dataExaminee = JasperReportsExportHelper.toXlsxFile(
                "jrxml/data-examinee.jrxml"
                , dataMapper.examinee(new ExamineeDto(), pageable).getContent()
        );
        zipFile.addFile(dataExaminee);
        dataExaminee.delete();

        File dataScore = JasperReportsExportHelper.toXlsxFile(
                "jrxml/data-scorer.jrxml"
                , dataMapper.scorer(new ScoreDto(), pageable).getContent()
        );
        zipFile.addFile(dataScore);
        dataScore.delete();

        // 나머지 가져오기
        // 0. 폴더위치 지정
        String jpgPath = "D:/jpg";
        // 1. 사진 폴더 생성
        File jpgFolder = new File(jpgPath);
        // 1.1 사진 가져옴
        File[] jpgList = jpgFolder.listFiles();
        // 1.2 사진 저장
        for (File f : jpgList) {
            if (f.isFile())
                zipFile.addFile("jpg", f);
        }


        String pdfPath = "D:/pdf";
        // 2. pdf 폴더 생성
        File pdfFolder = new File(pdfPath);
        // 2.1 pdf 가져옴
        File[] pdfList = pdfFolder.listFiles();
        // 2.2 pdf 저장
        for (File f : pdfList) {
            if (f.isFile())
                zipFile.addFile("pdf", f);
        }

        //
        byte[] ba = FileUtils.getByteArray(zipFile.getFile());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Set-Cookie", "fileDownload=true; path=/");
        headers.setContentType(MediaType.parseMediaType("application/zip"));
        headers.setContentLength(ba.length);
        headers.add("Content-Disposition", FileNameEncoder.encode("최종 산출물.zip"));
        return new ResponseEntity<>(ba, headers, HttpStatus.OK);
    }

    // 중간관리자 데이터
    @RequestMapping(value = "manager.zip", method = RequestMethod.GET)
    public ResponseEntity<byte[]> manager() throws ZipException {
        File file = new File("manager.zip");

        ZipFile zipFile = new ZipFile(file);

        String path = "D:/manager";

        File managerFolder = new File(path);
        File[] virtList = managerFolder.listFiles();

        for(File f : virtList){
            if(f.isFile())
                zipFile.addFile(f);
        }

        byte[] ba = FileUtils.getByteArray(zipFile.getFile());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Set-Cookie", "fileDownload=true; path=/");
        headers.setContentType(MediaType.parseMediaType("application/zip"));
        headers.setContentLength(ba.length);
        headers.add("Content-Disposition", FileNameEncoder.encode("중간관리자 데이터.zip"));

        return new ResponseEntity<>(ba, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "scorer.zip", method = RequestMethod.GET)
    public ResponseEntity<byte[]> scorer() throws ZipException {
        File file = new File("scorer.zip");

        ZipFile zipFile = new ZipFile(file);

        String jpgPath = "D:/jpg";
        String pdfPath = "D:/pdf";

        File jpgFolder = new File(jpgPath);
        File[] jpgList = jpgFolder.listFiles();

        for (File f : jpgList) {
            if(f.isFile())
                zipFile.addFile("jpg", f);
        }

        File pdfFolder = new File(pdfPath);
        File[] pdfList = pdfFolder.listFiles();

        for(File f : pdfList){
            if(f.isFile())
                zipFile.addFile("pdf", f);
        }

        //
        byte[] ba = FileUtils.getByteArray(zipFile.getFile());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Set-Cookie", "fileDownload=true; path=/");
        headers.setContentType(MediaType.parseMediaType("application/zip"));
        headers.setContentLength(ba.length);
        headers.add("Content-Disposition", FileNameEncoder.encode("평가위원 데이터.zip"));

        return new ResponseEntity<>(ba, headers, HttpStatus.OK);
    }
}