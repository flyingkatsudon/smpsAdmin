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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    private final HallDateRepository hallDateRepository;
    private final ExamineeRepository examineeRepository;
    private final ExamMapRepository examMapRepository;
    private final SheetRepository sheetRepository;
    private final ScoreRepository scoreRepository;
    private final ScoreLogRepository scoreLogRepository;
    private final GroupInfoRepository groupInfoRepository;

    // Windows
    @Value("${path.image.examinee:C:/api/smps}") String pathRoot;
    // Mac (smpsroot is different each)
    //@Value("${path.image.examinee:/Users/Jeremy/Humane/api/smps}") String pathRoot;

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
                        if (fkDevi != null) devi.setFkDevi(fkDevi);

                        // 2.3 편차 저장
                        devi = deviRepository.save(devi);
                    }
            );
            return ResponseEntity.ok("업로드가 완료되었습니다");
        } catch (Throwable throwable) {
            log.debug("{}", throwable.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("양식 파일을 확인하세요<br><br>" + throwable.getMessage());
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
            for (FormItemVo vo : itemList) {
                // 2. admission 변환, 저장

                Admission admission = mapper.convertValue(vo, Admission.class);
                admission = admissionRepository.save(admission);

                // 3. exam 변환, 저장
                Exam exam = mapper.convertValue(vo, Exam.class);

                log.debug("{}", vo);

                if (!vo.getFkExamCd().equals("") && vo.getFkExamCd() != null) {
                    Exam tmp = examRepository.findOne(new BooleanBuilder()
                            .and(QExam.exam.examCd.eq(vo.getFkExamCd()))
                    );
                    exam.setFkExam(tmp);
                }

                exam.setAdmission(admission);

                examRepository.save(exam);

                // 4. item 변환, 저장, 갯수비교
                if (Long.parseLong(vo.getItemCnt()) != uploadService.saveItems(vo)) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("항목 개수가 일치하지 않습니다!");
                }
            }
            return ResponseEntity.ok("업로드가 완료되었습니다");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            log.error("{}", throwable.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("양식 파일을 확인하세요<br><br>" + throwable.getMessage());
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
            hallList.forEach(vo -> {
                /**
                 * 제약조건 : 시험정보는 반드시 업로드 되어 있어야 한다.
                 */
                // 1. 시험정보 생성
                Exam exam = mapper.convertValue(vo, Exam.class);

                exam = examRepository.findOne(new BooleanBuilder()
                        .and(QExam.exam.examCd.eq(exam.getExamCd()))
                        .and(QExam.exam.examNm.eq(exam.getExamNm()))
                        .and(QExam.exam.examDate.eq(exam.getExamDate()))
                );

                // 2. 고사실정보 생성
                Hall hall = mapper.convertValue(vo, Hall.class);
                hall = hallRepository.save(hall);

                // 3. 응시고사실 생성
                ExamHall examHall = new ExamHall();
                examHall.setExam(exam);
                examHall.setHall(hall);


                // 4. 응시고사실 확인
                ExamHall tmp2 = examHallRepository.findOne(new BooleanBuilder()
                        .and(QExamHall.examHall.hall.hallCd.eq(examHall.getHall().getHallCd()))
                        .and(QExamHall.examHall.exam.examCd.eq(examHall.getExam().getExamCd()))
                );

                if (tmp2 != null) examHall.set_id(tmp2.get_id());

                // 5. 응시고사실 저장
                examHallRepository.save(examHall);

                // exam_hall_date 채우기
                ExamHallDate hallDate = new ExamHallDate();
                hallDate.setExam(exam);
                hallDate.setHall(hall);

                SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date date = transFormat.parse(vo.getHallDate());
                    hallDate.setHallDate(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // 가번호 정보가 없거나 공백이면 null로 저장
                if ((vo.getVirtNoEnd() == null && vo.getVirtNoStart() == null) ||
                        (vo.getVirtNoEnd() == "" && vo.getVirtNoStart() == "")) {

                    hallDate.setVirtNoStart(null);
                    hallDate.setVirtNoEnd(null);

                } else {
                    hallDate.setVirtNoStart(vo.getVirtNoStart());
                    hallDate.setVirtNoEnd(vo.getVirtNoEnd());
                }

                ExamHallDate tmp = hallDateRepository.findOne(new BooleanBuilder()
                        .and(QExamHallDate.examHallDate.hallDate.eq(hallDate.getHallDate()))
                        .and(QExamHallDate.examHallDate.hall.hallCd.eq(hallDate.getHall().getHallCd()))
                        .and(QExamHallDate.examHallDate.exam.examCd.eq(hallDate.getExam().getExamCd()))
                );

                if (tmp != null) hallDate.set_id(tmp.get_id());

                hallDateRepository.save(hallDate);

                /*GroupInfo find = groupInfoRepository.findOne(new BooleanBuilder()
                        .and(QGroupInfo.groupInfo.groupNm.eq(vo.getGroupNm()))
                        .and(QGroupInfo.groupInfo.deptNm.eq(vo.getDeptNm()))
                        .and(QGroupInfo.groupInfo.hallCd.eq(hall.getHallCd()))
                );

                if (find == null) {

                    GroupInfo groupInfo = new GroupInfo();
                    groupInfo.setDeptNm(vo.getDeptNm());
                    groupInfo.setGroupNm(vo.getGroupNm());
                    groupInfo.setHallCd(hall.getHallCd());

                    groupInfoRepository.save(groupInfo);
                }*/

            });
            return ResponseEntity.ok("업로드가 완료되었습니다");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            log.error("{}", throwable.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("양식 파일을 확인하세요<br><br>" + throwable.getMessage());
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
                /*QExam exam = QExamHall.examHall.exam;
                QHall hall = QExamHall.examHall.hall;

                ExamHall examHall = examHallRepository.findOne(new BooleanBuilder()
                        .and(exam.examDate.eq(dtf.parseLocalDateTime(vo.getExamDate()).toDate()))
                        // .and(hall.hallCd.eq(vo.getHallCd()))
                        .and(hall.headNm.eq(vo.getHeadNm()))
                        .and(hall.bldgNm.eq(vo.getBldgNm()))
                        .and(hall.hallNm.eq(vo.getHallNm()))
                        .and(exam.examCd.eq(vo.getExamCd())) // 위 5개 조건이 모두 같을 때, 시험코드로 구분하도록
                );*/

                // 시험정보
                Exam exam = examRepository.findOne(new BooleanBuilder()
                        .and(QExam.exam.examCd.eq(vo.getExamCd()))
                );

                // 1. ExamHallDate에서 시험 및 고사실 정보를 가져옴
                ExamHallDate hallDate = hallDateRepository.findOne(new BooleanBuilder()
                        .and(QExamHallDate.examHallDate.exam.examCd.eq(vo.getExamCd()))
                        .and(QExamHallDate.examHallDate.hallDate.eq(dtf.parseLocalDateTime(vo.getExamDate()).toDate()))
                        .and(QExamHallDate.examHallDate.hall.headNm.eq(vo.getHeadNm()))
                        .and(QExamHallDate.examHallDate.hall.bldgNm.eq(vo.getBldgNm()))
                        .and(QExamHallDate.examHallDate.hall.hallNm.eq(vo.getHallNm()))
                );

                // 3. 수험생정보 생성
                Examinee examinee = mapper.convertValue(vo, Examinee.class);
                examineeRepository.save(examinee);

                if (vo.getGroupNm().length() == 0) vo.setGroupNm(null); // 조 정보가 없으면 null로 처리

                //ExamMap examMap = new ExamMap();
                ExamMap examMap = mapper.convertValue(vo, ExamMap.class);
                examMap.setExam(hallDate.getExam());
                examMap.setHall(hallDate.getHall());
                examMap.setExam(exam);
                examMap.setExaminee(examinee);

                ExamMap tmp = examMapRepository.findOne(new BooleanBuilder()
                        .and(QExamMap.examMap.exam.examCd.eq(examMap.getExam().getExamCd()))
                        .and(QExamMap.examMap.examinee.examineeCd.eq(examMap.getExaminee().getExamineeCd()))
                );

                if (tmp != null) examMap.set_id(tmp.get_id());

                // 3.1 수험생정보 저장
                examMapRepository.save(examMap);
            });
            return ResponseEntity.ok("업로드가 완료되었습니다");
        } catch (Throwable throwable) {
            log.error("{}", throwable.getMessage());
            throwable.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("양식 파일을 확인하세요<br><br>" + throwable.getMessage());
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
            return ResponseEntity.ok("업로드가 완료되었습니다");
        } catch (Throwable throwable) {
            log.error("{}", throwable.getMessage());
            throwable.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("양식 파일을 확인하세요");
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
            return ResponseEntity.ok("업로드가 완료되었습니다");
        } catch (Throwable throwable) {
            log.error("{}", throwable.getMessage());
            throwable.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("양식 파일을 확인하세요");
        }
    }

    @Data
    private class FileWrapper<T> {
        private String hallCd;
        private List<T> content;
        private Long totalCount;
    }
}
