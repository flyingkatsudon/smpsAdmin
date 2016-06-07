package com.humane.smps.controller;

import com.blogspot.na5cent.exom.ExOM;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.reflect.TypeToken;
import com.humane.smps.form.FormDeviVo;
import com.humane.smps.form.FormExamineeVo;
import com.humane.smps.form.FormHallVo;
import com.humane.smps.form.FormItemVo;
import com.humane.smps.model.*;
import com.humane.smps.repository.*;
import com.humane.smps.service.UploadService;
import com.humane.util.file.FileUtils;
import com.humane.util.zip4j.ZipFile;
import com.mysema.query.BooleanBuilder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "upload")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UploadController {
    private final UploadService uploadService;
    private final AdmissionRepository admissionRepository;
    private final DeviRepository deviRepository;
    private final ExamRepository examRepository;
    private final HallRepository hallRepository;
    private final ExamHallRepository examHallRepository;
    private final ExamineeRepository examineeRepository;
    private final ExamMapRepository examMapRepository;
    private final SheetRepository sheetRepository;
    private final ScoreRepository scoreRepository;

    @Value("${path.image.examinee:C:/api/smps}") String pathRoot;

    @RequestMapping(value = "devi", method = RequestMethod.POST)
    public void devi(@RequestPart("file") MultipartFile multipartFile) throws Throwable {
        File file = FileUtils.saveFile(new File(pathRoot, "setting"), multipartFile);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            // 1 엑셀 파징
            List<FormDeviVo> deviList = ExOM.mapFromExcel(file).to(FormDeviVo.class).map(1);

            // 2. 편차 생성
            deviList.forEach(vo -> {
                        log.debug("{}", vo);

                        // 2.1 편차 변환
                        Devi devi = mapper.convertValue(vo, Devi.class);

                        // 2.2 fk 설정
                        Devi fkDevi = deviRepository.findOne(vo.getFkDeviCd());
                        if (fkDevi != null) devi.setDevi(fkDevi);

                        // 2.3 편차 저장
                        devi = deviRepository.save(devi);
                    }
            );
        } catch (Throwable throwable) {
            log.debug("{}", throwable.getMessage());
        }
    }

    @RequestMapping(value = "item", method = RequestMethod.POST)
    public ResponseEntity item(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        // 파일이 없울경우 에러 리턴.
        if (multipartFile.isEmpty()) return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(null);

        File file = FileUtils.saveFile(new File(pathRoot, "setting"), multipartFile);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            // 1. excel 변환
            List<FormItemVo> itemList = ExOM.mapFromExcel(file).to(FormItemVo.class).map(1);
            for (FormItemVo dto : itemList) {
                // 2. admission 변환, 저장
                log.debug("{}", dto);
                Admission admission = mapper.convertValue(dto, Admission.class);
                admission = admissionRepository.save(admission);

                // 3. exam 변환, 저장
                Exam exam = mapper.convertValue(dto, Exam.class);
                exam.setAdmission(admission);
                examRepository.save(exam);

                // 4. item 변환, 저장, 갯수비교
                if (Long.parseLong(dto.getItemCnt()) != uploadService.saveItems(dto)) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("항목 갯수가 일치하지 않습니다!.");
                }
            }
            return ResponseEntity.ok("데이터 정상 처리 완료");
        } catch (Throwable t) {
            t.printStackTrace();
            log.error("{}", t.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("데이터에러. 서버 로그 확인 바람.");
        } finally {
            file.delete();
        }
    }

    @RequestMapping(value = "hall", method = RequestMethod.POST)
    public void hall(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        File file = FileUtils.saveFile(new File(pathRoot, "setting"), multipartFile);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            List<FormHallVo> hallList = ExOM.mapFromExcel(file).to(FormHallVo.class).map(1);
            hallList.forEach(uploadHallDto -> {
                /**
                 * 제약조건 : 시험정보는 반드시 업로드 되어 있어야 한다.
                 */

                // 1. 시험정보 생성
                Exam exam = mapper.convertValue(uploadHallDto, Exam.class);
                exam = examRepository.findOne(new BooleanBuilder()
                        .and(QExam.exam.examNm.eq(exam.getExamNm()))
                        .and(QExam.exam.examDate.eq(exam.getExamDate()))
                        .and(QExam.exam.examTime.eq(exam.getExamTime()))
                );

                // 2. 고사실정보 생성
                Hall hall = mapper.convertValue(uploadHallDto, Hall.class);
                hall = hallRepository.save(hall);

                // 3. 응시고사실 생성
                ExamHall examHall = new ExamHall();
                examHall.setExam(exam);
                examHall.setHall(hall);

                // 4. 응시고사실 저장
                examHallRepository.save(examHall);
            });
        } catch (Throwable throwable) {
            log.error("{}", throwable.getMessage());
        } finally {
            file.delete();
        }
    }

    @RequestMapping(value = "examinee", method = RequestMethod.POST)
    public void examinee(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        File file = FileUtils.saveFile(new File(pathRoot, "setting"), multipartFile);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

        try {
            List<FormExamineeVo> examineeList = ExOM.mapFromExcel(file).to(FormExamineeVo.class).map(1);
            examineeList.forEach(vo -> {
                log.debug("{}", vo);

                // 1. ExamHall 에서 고사실 및 시험정보를 가져온다.
                QExam exam = QExamHall.examHall.exam;
                QHall hall = QExamHall.examHall.hall;

                BooleanBuilder builder = new BooleanBuilder()
                        .and(exam.examDate.eq(dtf.parseLocalDateTime(vo.getExamDate()).toDate()))
                        .and(exam.examTime.eq(dtf.parseLocalDateTime(vo.getExamTime()).toDate()))
                        .and(hall.headNm.eq(vo.getHeadNm()))
                        .and(hall.bldgNm.eq(vo.getBldgNm()))
                        .and(hall.hallNm.eq(vo.getHallNm()));

                ExamHall examHall = examHallRepository.findOne(builder);
                log.debug("{}", examHall);

                // 3. 수험생정보 생성
                Examinee examinee = mapper.convertValue(vo, Examinee.class);
                examineeRepository.save(examinee);

                ExamMap examMap = new ExamMap();
                examMap.setExam(examHall.getExam());
                examMap.setHall(examHall.getHall());
                examMap.setExaminee(examinee);

                // 3.1 수험생정보 저장
                examMapRepository.save(examMap);
            });
        } catch (Throwable throwable) {
            log.error("{}", throwable.getMessage());
            throwable.printStackTrace();
        } finally {
            file.delete();
        }
    }

    @RequestMapping(value = "scoreEndData", method = RequestMethod.POST)
    public void scoreEndData(@RequestPart("file") MultipartFile multipartFile) throws ZipException, IOException {
        File file = FileUtils.saveFile(new File(pathRoot, "data"), multipartFile);

        // zip4j 읽기
        ZipFile zipFile = new ZipFile(file);
        zipFile.setFileNameCharset(zipFile.getCharset());

        List<FileHeader> fileHeaders = zipFile.getFileHeaders();
        for (FileHeader fileHeader : fileHeaders) {
            String fileName = fileHeader.getFileName();
            if (fileName.endsWith("_sheet.txt")) {

                // file read
                FileWrapper<Sheet> wrapper = zipFile.parseObject(fileHeader, new TypeToken<FileWrapper<Sheet>>() {
                });
                log.debug("{}", wrapper.getContent());

                QSheet qSheet = QSheet.sheet;

                wrapper.getContent().forEach(sheet -> {
                    Sheet tmp = sheetRepository.findOne(
                            new BooleanBuilder()
                                    .and(qSheet.scorerNm.eq(sheet.getScorerNm()))
                                    .and(qSheet.sheetNo.eq(sheet.getSheetNo()))
                                    .and(qSheet.exam.examCd.eq(sheet.getExam().getExamCd()))
                    );

                    if (tmp != null) sheet.set_id(tmp.get_id());

                    sheetRepository.save(sheet);

                });

            } else if (fileName.endsWith("_score.txt")) {
                FileWrapper<Score> wrapper = zipFile.parseObject(fileHeader, new TypeToken<FileWrapper<Score>>() {
                });

                log.debug("{}", wrapper.getContent());

                QScore qScore = QScore.score;

                wrapper.getContent().forEach(score -> {

                    Score tmp = scoreRepository.findOne(
                            new BooleanBuilder()
                                    .and(qScore.exam.examCd.eq(score.getExam().getExamCd()))
                                    .and(qScore.virtNo.eq(score.getVirtNo()))
                                    .and(qScore.scorerNm.eq(score.getScorerNm()))
                    );

                    if (tmp != null) score.set_id(tmp.get_id());

                    scoreRepository.save(score);
                });
            } else if (fileName.endsWith(".pdf")) {
                zipFile.extractFile(fileHeader, "D:/pdf");
            } else if (fileName.endsWith(".jpg")) {
                zipFile.extractFile(fileHeader, "D:/jpg");
            }
        }
    }

    @RequestMapping(value = "manager", method = RequestMethod.POST)
    public void manager(@RequestPart("file") MultipartFile multipartFile) throws ZipException, IOException {
        File file = FileUtils.saveFile(new File(pathRoot, "data"), multipartFile);

        // zip4j 읽기
        ZipFile zipFile = new ZipFile(file);
        zipFile.setFileNameCharset(zipFile.getCharset());

        List<FileHeader> fileHeaders = zipFile.getFileHeaders();
    }

    @Data
    private class FileWrapper<T> {
        private String hallCd;
        private List<T> content;
        private Long totalCount;
    }
}
