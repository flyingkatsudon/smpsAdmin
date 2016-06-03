package com.humane.smps.controller;

import com.humane.smps.dto.*;
import com.humane.smps.mapper.AdminScoreMapper;
import com.humane.smps.mapper.CheckMapper;
import com.humane.smps.mapper.DataMapper;
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

        // 압축파일 파라미터 설정
        ZipParameters parameters = new ZipParameters();
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

        // entry 생성
        File fileDept = JasperReportsExportHelper.toXlsxFile(
                "jrxml/status-dept.jrxml"
                , statusMapper.dept(new StatusDeptDto(), pageable).getContent());
        zipFile.addFile(fileDept, parameters);
        fileDept.delete();

        File fileMajor = JasperReportsExportHelper.toXlsxFile(
                "jrxml/status-major.jrxml"
                , statusMapper.major(new StatusMajorDto(), pageable).getContent());
        zipFile.addFile(fileMajor, parameters);
        fileMajor.delete();

        //1. xlsx 파일 생성
        File fileHall = JasperReportsExportHelper.toXlsxFile(
                "jrxml/status-hall.jrxml"
                , statusMapper.hall(new StatusHallDto(), pageable).getContent());
        //2. zip파일에 추가시키기
        zipFile.addFile(fileHall, parameters);
        //3. 추가시킨 후 xlsx파일 삭제
        fileHall.delete();

        // 1. xlsx 파일 생성
        File fileGroup = JasperReportsExportHelper.toXlsxFile(
                "jrxml/status-group.jrxml"
                , statusMapper.group(new StatusGroupDto(), pageable).getContent()
        );
        zipFile.addFile(fileGroup, parameters);
        fileGroup.delete();

        File dataExaminee = JasperReportsExportHelper.toXlsxFile(
                "jrxml/data-examinee.jrxml"
                , dataMapper.examinee(new ExamineeDto(), pageable).getContent()
        );
        zipFile.addFile(dataExaminee, parameters);
        dataExaminee.delete();

        File dataScore = JasperReportsExportHelper.toXlsxFile(
                "jrxml/data-scorer.jrxml"
                , dataMapper.score(new ScoreDto(), pageable).getContent()
        );
        zipFile.addFile(dataScore, parameters);
        dataScore.delete();

        // 나머지 가져오기
        // 0. 폴더위치 지정
        String jpgPath = "D:/jpg";
        String pdfPath = "D:/pdf";
        // 1. 사진 폴더 생성
        File jpgFolder = new File(jpgPath);
        // 1.1 사진 가져옴
        File[] jpgList = jpgFolder.listFiles();
        parameters.setRootFolderInZip("jpg");
        //zipFile.addFolder(jpgFolder, parameters);
        for (File f : jpgList) {
            if (f.isFile()) {
                log.debug("{}", f.getName());

                zipFile.addFile(f, parameters);
            }
        }


        // 1.2 사진 저장

        // 2. pdf 폴더 생성
        // 2.1 pdf 가져옴
        // 2.2 pdf 저장

        //
        byte[] ba = FileUtils.getByteArray(zipFile.getFile());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Set-Cookie", "fileDownload=true; path=/");
        headers.setContentType(MediaType.parseMediaType("application/zip"));
        headers.add("Content-Disposition", FileNameEncoder.encode("최종산출물.zip"));
        return new ResponseEntity<>(ba, headers, HttpStatus.OK);
    }
}