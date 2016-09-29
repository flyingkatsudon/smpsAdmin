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
import com.querydsl.core.BooleanBuilder;
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
    private final ScoreLogRepository scoreLogRepository;

    @Value("${path.image.examinee:C:/api/smps}") String pathRoot;

    @RequestMapping(value = "devi", method = RequestMethod.POST)
    public ResponseEntity<String> devi(@RequestPart("file") MultipartFile multipartFile) throws Throwable {
        File file = FileUtils.saveFile(new File(pathRoot, "setting"), multipartFile);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            // 1 엑셀 파징
            List<FormDeviVo> deviList = ExOM.mapFromExcel(file).to(FormDeviVo.class).map(1);

            log.debug("{}", deviList);
            // 2. 편차 생성
            deviList.forEach(vo -> {
                        // 2.1 편차 변환
                        Devi devi = mapper.convertValue(vo, Devi.class);

                        // 2.2 fk 설정
                        Devi fkDevi = deviRepository.findOne(vo.getFkDeviCd());
                        if (fkDevi != null) devi.setDevi(fkDevi);

                        // 2.3 편차 저장
                        devi = deviRepository.save(devi);
                    }
            );
            return ResponseEntity.ok("업로드가 완료되었습니다.");
        } catch (Throwable throwable) {
            log.debug("{}", throwable.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("양식 파일을 확인하세요.");
        }
    }

    @RequestMapping(value = "item", method = RequestMethod.POST)
    public ResponseEntity<String> item(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        // 파일이 없울경우 에러 리턴.
        if (multipartFile.isEmpty()) return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(null);

        File file = FileUtils.saveFile(new File(pathRoot, "setting"), multipartFile);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            // 1. excel 변환
            List<FormItemVo> itemList = ExOM.mapFromExcel(file).to(FormItemVo.class).map(1);
            log.debug("{}", itemList);
            for (FormItemVo dto : itemList) {
                // 2. admission 변환, 저장

                Admission admission = mapper.convertValue(dto, Admission.class);
                admission = admissionRepository.save(admission);

                // 3. exam 변환, 저장
                Exam exam = mapper.convertValue(dto, Exam.class);
                exam.setAdmission(admission);
                /*exam.setVirtNoDigits(Integer.parseInt(dto.getVirtNoDigits()));*/
                examRepository.save(exam);

                // 4. item 변환, 저장, 갯수비교
                if (Long.parseLong(dto.getItemCnt()) != uploadService.saveItems(dto)) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("항목 개수가 일치하지 않습니다!.");
                }
            }
            return ResponseEntity.ok("업로드가 완료되었습니다.");
        } catch (Throwable t) {
            t.printStackTrace();
            log.error("{}", t.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("양식 파일을 확인하세요.");
        }
    }

    @RequestMapping(value = "hall", method = RequestMethod.POST)
    public ResponseEntity<String> hall(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        File file = FileUtils.saveFile(new File(pathRoot, "setting"), multipartFile);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            List<FormHallVo> hallList = ExOM.mapFromExcel(file).to(FormHallVo.class).map(1);
            log.debug("{}", hallList);
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

                // 4. 응시고사실 확인
                ExamHall tmp = examHallRepository.findOne(new BooleanBuilder()
                        .and(QExamHall.examHall.hall.hallCd.eq(examHall.getHall().getHallCd()))
                        .and(QExamHall.examHall.exam.examCd.eq(examHall.getExam().getExamCd()))
                );

                if (tmp != null) examHall.set_id(tmp.get_id());

                // 5. 응시고사실 저장
                examHallRepository.save(examHall);
            });
            return ResponseEntity.ok("업로드가 완료되었습니다.");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            log.error("{}", throwable.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("양식 파일을 확인하세요.");
        }
    }

    @RequestMapping(value = "examinee", method = RequestMethod.POST)
    public ResponseEntity<String> examinee(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        File file = FileUtils.saveFile(new File(pathRoot, "setting"), multipartFile);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

        try {
            List<FormExamineeVo> examineeList = ExOM.mapFromExcel(file).to(FormExamineeVo.class).map(1);
            log.debug("{}", examineeList);
            examineeList.forEach(vo -> {
                // 1. ExamHall 에서 고사실 및 시험정보를 가져온다.
                QExam exam = QExamHall.examHall.exam;
                QHall hall = QExamHall.examHall.hall;

                ExamHall examHall = examHallRepository.findOne(new BooleanBuilder()
                        .and(exam.examDate.eq(dtf.parseLocalDateTime(vo.getExamDate()).toDate()))
                        .and(exam.examTime.eq(dtf.parseLocalDateTime(vo.getExamTime()).toDate()))
                        .and(hall.headNm.eq(vo.getHeadNm()))
                        .and(hall.bldgNm.eq(vo.getBldgNm()))
                        .and(hall.hallNm.eq(vo.getHallNm()))
                );

                // 3. 수험생정보 생성
                Examinee examinee = mapper.convertValue(vo, Examinee.class);
                examineeRepository.save(examinee);

                //ExamMap examMap = new ExamMap();
                ExamMap examMap = mapper.convertValue(vo, ExamMap.class);
                examMap.setExam(examHall.getExam());
                examMap.setHall(examHall.getHall());
                examMap.setExaminee(examinee);

                ExamMap tmp = examMapRepository.findOne(new BooleanBuilder()
                        .and(QExamMap.examMap.exam.examCd.eq(examMap.getExam().getExamCd()))
                        .and(QExamMap.examMap.examinee.examineeCd.eq(examMap.getExaminee().getExamineeCd()))
                );

                if (tmp != null) examMap.set_id(tmp.get_id());

                // 3.1 수험생정보 저장
                examMapRepository.save(examMap);
            });
            return ResponseEntity.ok("업로드가 완료되었습니다.");
        } catch (Throwable throwable) {
            log.error("{}", throwable.getMessage());
            throwable.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("양식 파일을 확인하세요.");
        }
    }

    @RequestMapping(value = "scoreEndData", method = RequestMethod.POST)
    public ResponseEntity<String> scoreEndData(@RequestPart("file") MultipartFile multipartFile) throws ZipException, IOException {
        File file = FileUtils.saveFile(new File(pathRoot, "data"), multipartFile);

        // zip4j 읽기
        ZipFile zipFile = new ZipFile(file);
        String charset = zipFile.getCharset();
        log.debug("Detected charset : {}", charset);
        zipFile.setFileNameCharset(charset);

        try {
            List<FileHeader> fileHeaders = zipFile.getFileHeaders();
            for (FileHeader fileHeader : fileHeaders) {
                String fileName = fileHeader.getFileName();
                if (fileName.endsWith("_sheet.txt")) {
                    // file read
                    FileWrapper<Sheet> wrapper = zipFile.parseObject(fileHeader, new TypeToken<FileWrapper<Sheet>>() {
                    }, charset);

                    QSheet qSheet = QSheet.sheet;

                    wrapper.getContent().forEach(sheet -> {
                        Sheet tmp = null;
                        tmp = sheetRepository.findOne(
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
                    }, charset);

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
                } else if (fileName.endsWith("_scoreLog.txt")) {
                    FileWrapper<ScoreLog> wrapper = zipFile.parseObject(fileHeader, new TypeToken<FileWrapper<ScoreLog>>() {
                    }, charset);
                    QScoreLog qScoreLog = QScoreLog.scoreLog;

                    wrapper.getContent().forEach(scoreLog -> {
                        try {
                            ScoreLog tmp = scoreLogRepository.findOne(
                                    new BooleanBuilder()
                                            .and(qScoreLog.exam.examCd.eq(scoreLog.getExam().getExamCd()))
                                            .and(qScoreLog.hall.hallCd.eq(scoreLog.getHall().getHallCd()))
                                            .and(qScoreLog.scorerNm.eq(scoreLog.getScorerNm()))
                                            .and(qScoreLog.logDttm.eq(scoreLog.getLogDttm()))
                                            .and(qScoreLog.virtNo.eq(scoreLog.getVirtNo()))
                            );

                            if (tmp == null) {
                                scoreLogRepository.save(scoreLog);
                            }
                        } catch (Exception e) {
                            log.error("{}", e.getMessage());
                        }
                    });
                } else if (fileName.endsWith(".pdf")) {
                    zipFile.extractFile(fileHeader, pathRoot + "/pdf");
                } else if (fileName.endsWith(".jpg")) {
                    zipFile.extractFile(fileHeader, pathRoot + "/jpg");
                }
            }
            return ResponseEntity.ok("업로드가 완료되었습니다.");
        } catch (Throwable throwable) {
            log.error("{}", throwable.getMessage());
            throwable.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("양식 파일을 확인하세요.");
        }
    }

    @RequestMapping(value = "manager", method = RequestMethod.POST)
    public ResponseEntity<String> manager(@RequestPart("file") MultipartFile multipartFile) throws ZipException, IOException {
        File file = FileUtils.saveFile(new File(pathRoot, "smpsMgr"), multipartFile);

        // zip4j 읽기
        ZipFile zipFile = new ZipFile(file);
        String charset = zipFile.getCharset();
        log.debug("Detected charset : {}", charset);
        zipFile.setFileNameCharset(charset);

        try {
            List<FileHeader> fileHeaders = zipFile.getFileHeaders();
            for (FileHeader fileHeader : fileHeaders) {
                String fileName = fileHeader.getFileName();
                if (fileName.endsWith("_ExamMap.txt")) {
                    // file read
                    FileWrapper<ExamMap> wrapper = zipFile.parseObject(fileHeader, new TypeToken<FileWrapper<ExamMap>>() {
                    }, charset);

                    QExamMap qExamMap = QExamMap.examMap;

                    wrapper.getContent().forEach(examMap -> {
                        ExamMap tmp = null;
                        tmp = examMapRepository.findOne(
                                new BooleanBuilder()
                                        .and(qExamMap.examinee.examineeCd.eq(examMap.getExaminee().getExamineeCd()))
                                        .and(qExamMap.hall.hallCd.eq(examMap.getHall().getHallCd()))
                                        .and(qExamMap.exam.examCd.eq(examMap.getExam().getExamCd()))
                        );

                        if (tmp != null) examMap.set_id(tmp.get_id());
                        examMapRepository.save(examMap);
                    });
                } else if (fileName.endsWith(".jpg")) {
                    zipFile.extractFile(fileHeader, pathRoot + "/smpsMgr/jpg");
                }
            }
            return ResponseEntity.ok("업로드가 완료되었습니다.");
        } catch (Throwable throwable) {
            log.error("{}", throwable.getMessage());
            throwable.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("양식 파일을 확인하세요.");
        }
    }

    @Data
    private class FileWrapper<T> {
        private String hallCd;
        private List<T> content;
        private Long totalCount;
    }
}
