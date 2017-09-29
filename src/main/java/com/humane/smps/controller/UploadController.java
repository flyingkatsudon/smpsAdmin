package com.humane.smps.controller;

import com.blogspot.na5cent.exom.ExOM;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.reflect.TypeToken;
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
import org.apache.commons.lang.StringUtils;
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
    private final ExamRepository examRepository;
    private final HallRepository hallRepository;
    private final HallDateRepository hallDateRepository;
    private final DebateHallRepository debateHallRepository;
    private final ExamineeRepository examineeRepository;
    private final ExamMapRepository examMapRepository;
    private final SheetRepository sheetRepository;
    private final ScoreRepository scoreRepository;
    private final ScoreLogRepository scoreLogRepository;

    // Windows
    @Value("${path.smps:C:/api/smps}") String pathRoot;
    // Mac (smpsroot is different each)
    //@Value("${path.smps:/Users/Jeremy/Humane/api/smps}") String pathRoot;

    // 고려대 면접고사용
    public String validate(String str){
        if(str.equals("") || str == null) return null;
        else return str;
    }

    // 고려대 면접고사용
    @RequestMapping(value = "order", method = RequestMethod.POST)
    public ResponseEntity<String> order(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        // 파일이 없울경우 에러 리턴.
        if (multipartFile.isEmpty()) return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(null);

        File file = FileUtils.saveFile(new File(pathRoot, "setting"), multipartFile);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            // 1. excel 변환
            List<FormExamineeVo> orderList = ExOM.mapFromExcel(file).to(FormExamineeVo.class).map(1);
            log.debug("{}", orderList);

            // 2. 각 수험생 별 순번 저장
            for (FormExamineeVo vo : orderList) {

                ExamMap examMap = examMapRepository.findOne(new BooleanBuilder()
                        .and(QExamMap.examMap.exam.admission.admissionCd.eq(vo.getAdmissionCd()))
                        .and(QExamMap.examMap.exam.examCd.eq(vo.getExamCd()))
                        .and(QExamMap.examMap.examinee.examineeCd.eq(vo.getExamineeCd()))
                );

                if (examMap != null) {
                    if (vo.getIsAttend()) {
                        examMap.setGroupNm(validate(vo.getGroupNm()));
                        examMap.setGroupOrder(validate(vo.getGroupOrder()));
                        examMap.setDebateNm(validate(vo.getDebateNm()));
                        examMap.setDebateOrder(validate(vo.getDebateOrder()));
                    } else {
                        examMap.setGroupNm(null);
                        examMap.setGroupOrder(null);
                        examMap.setDebateNm(null);
                        examMap.setDebateOrder(null);
                    }
                    examMapRepository.save(examMap);
                }
            }

            return ResponseEntity.ok("업로드가 완료되었습니다");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            log.error("{}", throwable.getMessage());
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

                if (!vo.getFkExamCd().equals("") && vo.getFkExamCd() != null) {
                    Exam tmp = examRepository.findOne(new BooleanBuilder()
                            .and(QExam.exam.examCd.eq(vo.getFkExamCd()))
                    );
                    exam.setFkExam(tmp);
                }

                switch(vo.getVirtNoAssignType()){
                    case "가번호":
                        exam.setVirtNoAssignType("virtNo");
                        break;
                    case "관리번호":
                        exam.setVirtNoAssignType("manageNo");
                        break;
                    case "수험번호":
                        exam.setVirtNoAssignType("examineeCd");
                        break;
                    default:
                        exam.setVirtNoAssignType("virtNo");
                        break;
                }

                if(vo.getBarcodeType().equals("")) exam.setBarcodeType(null);

                exam.setAdmission(admission);

                examRepository.save(exam);

                // 4. item 변환, 저장, 갯수비교
                if (Long.parseLong(vo.getItemCnt()) != uploadService.saveItems(vo)) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("항목 개수가 일치하지 않습니다<br><br>시험코드: " + vo.getExamCd() + " / 시험명: " + vo.getExamNm());
                }
            }
            return ResponseEntity.ok("업로드가 완료되었습니다");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
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
            for(FormHallVo vo : hallList){
                log.debug("{}", vo.getAdmissionCd() + "/" + vo.getExamCd());
                if (vo != null && StringUtils.isNotEmpty(vo.getAdmissionCd())) {
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

                    // 3. 응시고사실(exam_hall_date) 채우기
                    ExamHallDate hallDate = new ExamHallDate();
                    hallDate.setExam(exam);
                    hallDate.setHall(hall);

                    // 날짜 변환만 확인
                    SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date date = transFormat.parse(vo.getHallDate());
                        hallDate.setHallDate(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("시험일자의 형식을 확인하세요<br><br>시험코드: " + vo.getExamCd() + " / 시험일자: " + vo.getExamDate());
                    }

                    // 가번호 정보가 없거나 공백이면 null로 저장
                    if ((vo.getVirtNoEnd() == null && vo.getVirtNoStart() == null) ||
                            (vo.getVirtNoEnd().equals("") && vo.getVirtNoStart().equals(""))) {

                        hallDate.setVirtNoStart(null);
                        hallDate.setVirtNoEnd(null);

                    } else {
                        hallDate.setVirtNoStart(vo.getVirtNoStart());
                        hallDate.setVirtNoEnd(vo.getVirtNoEnd());
                    }

                    // 응시고사실 확인
                    try {
                        ExamHallDate tmp = hallDateRepository.findOne(new BooleanBuilder()
                                .and(QExamHallDate.examHallDate.hallDate.eq(hallDate.getHallDate()))
                                .and(QExamHallDate.examHallDate.hall.hallCd.eq(hallDate.getHall().getHallCd()))
                                .and(QExamHallDate.examHallDate.exam.examCd.eq(hallDate.getExam().getExamCd()))
                        );

                        if (tmp != null) hallDate.set_id(tmp.get_id());

                        hallDateRepository.save(hallDate);

                        // 고려대 면접고사용
                        ExamDebateHall examDebateHall = new ExamDebateHall();

                        ExamDebateHall t = debateHallRepository.findOne(new BooleanBuilder()
                                .and(QExamDebateHall.examDebateHall.hallCd.eq(vo.getHallCd()))
                                .and(QExamDebateHall.examDebateHall.groupNm.eq(vo.getGroupNm()))
                        );

                        if (t != null) {
                            examDebateHall.set_id(t.get_id());
                        }

                        examDebateHall.setGroupNm(vo.getGroupNm());
                        examDebateHall.setHallCd(vo.getHallCd());

                        debateHallRepository.save(examDebateHall);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("시험코드와 시험일자를 확인하세요<br><br>시험코드: " + vo.getExamCd() + " / 시험일자: " + vo.getHallDate());
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("'전형코드' 열이 없거나 비어있습니다");
                }
            }
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

            for(FormExamineeVo vo : examineeList){
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
                Examinee examinee = null;
                try {
                    examinee = mapper.convertValue(vo, Examinee.class);
                    examineeRepository.save(examinee);

                    ExamMap examMap = mapper.convertValue(vo, ExamMap.class);
                    examMap.setExam(exam);
                    examMap.setExaminee(examinee);
                    examMap.setHall(hallDate.getHall());

                    // exam.virt_no_assign_type에 따라 virtNo를 자동으로 채워넣어야함
                    if (exam.getVirtNoAssignType().equals("examineeCd")) {
                        examMap.setVirtNo(examinee.getExamineeCd());
                    }

                    if (vo.getGroupNm() != null && vo.getGroupNm().equals("")) {
                            //return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("올바른 조 정보가 아닙니다. '조' 열을 삭제하거나 다시 만드신 후 재시도하세요");
                            examMap.setGroupNm(null); // 조 정보가 없으면 null로 처리
                    }

                    ExamMap tmp = examMapRepository.findOne(new BooleanBuilder()
                            .and(QExamMap.examMap.exam.examCd.eq(examMap.getExam().getExamCd()))
                            .and(QExamMap.examMap.examinee.examineeCd.eq(examMap.getExaminee().getExamineeCd()))
                    );

                    if (tmp != null) {
                        examMap.set_id(tmp.get_id());

                        // exam.virt_no_assign_type에 따라 virtNo를 자동으로 채워넣어야함
                        if (tmp.getExam().getVirtNoAssignType().equals("examineeCd")) {
                            tmp.setVirtNo(examinee.getExamineeCd());
                        }
                    }

                    // 3.1 수험생정보 저장
                    examMapRepository.save(examMap);
                } catch (Exception e) {
                    e.printStackTrace();
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("올바른 수험생 정보가 아닙니다. 수험생의 모든 정보가 올바른지 확인하세요<br><br>수험번호: " + vo.getExamineeCd() + " / 고사실명: " + vo.getHallNm());
                }
            }
            return ResponseEntity.ok("업로드가 완료되었습니다");
        } catch (Throwable throwable) {
            log.error("{}", throwable.getMessage());
            throwable.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("하나의 시트인지, 데이터가 누락된 수험생이 있는지 양식 파일을 확인하세요<br><br>" + throwable.getMessage());
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

                    wrapper.getContent().forEach(sheet -> {
                        try {
                            Sheet tmp = sheetRepository.findOne(new BooleanBuilder()
                                    .and(QSheet.sheet.scorerNm.eq(sheet.getScorerNm()))
                                    .and(QSheet.sheet.sheetNo.eq(sheet.getSheetNo()))
                                    .and(QSheet.sheet.exam.examCd.eq(sheet.getExam().getExamCd()))
                            );

                            if (tmp != null) sheet.set_id(tmp.get_id());

                            sheetRepository.save(sheet);
                        } catch (Exception e) {
                            log.error("{}", e.getMessage());
                        }
                    });

                } else if (fileName.endsWith("_score.txt")) {
                    FileWrapper<Score> wrapper = zipFile.parseObject(fileHeader, new TypeToken<FileWrapper<Score>>() {
                    }, charset);

                    wrapper.getContent().forEach(score -> {
                        try {
                            // score에 입력될 데이터에 수험생 정보가 있는지 검사
                            ExamMap examMap = examMapRepository.findOne(new BooleanBuilder()
                                    .and(QExamMap.examMap.exam.examCd.eq(score.getExam().getExamCd()))
                                    .and(QExamMap.examMap.examinee.examineeCd.eq(score.getVirtNo()))
                            );

                            String virtNoAssignType = score.getExam().getVirtNoAssignType();

                            // 가번호 할당 방식이 '수험번호'라면 가번호 자리에 수험번호를 채움
                            if (virtNoAssignType != null && virtNoAssignType.equals("examineeCd")) {
                                examMap.setVirtNo(score.getVirtNo());
                                examMapRepository.save(examMap);
                            }

                            // 기존에 입력된 점수가 있는지 검사
                            Score tmp = scoreRepository.findOne(new BooleanBuilder()
                                    .and(QScore.score.exam.examCd.eq(score.getExam().getExamCd()))
                                    .and(QScore.score.virtNo.eq(score.getVirtNo()))
                                    .and(QScore.score.scorerNm.eq(score.getScorerNm()))
                            );
                            if (tmp != null) score.set_id(tmp.get_id());

                            scoreRepository.save(score);
                        } catch (Exception e) {
                            e.printStackTrace();
                            log.error("{}", e.getMessage());
                        }
                    });
                } else if (fileName.endsWith("_scoreLog.txt")) {
                    FileWrapper<ScoreLog> wrapper = zipFile.parseObject(fileHeader, new TypeToken<FileWrapper<ScoreLog>>() {
                    }, charset);

                    wrapper.getContent().forEach(scoreLog -> {
                        try {
                            ScoreLog tmp = scoreLogRepository.findOne(new BooleanBuilder()
                                    .and(QScoreLog.scoreLog.exam.examCd.eq(scoreLog.getExam().getExamCd()))
                                    .and(QScoreLog.scoreLog.hall.hallCd.eq(scoreLog.getHall().getHallCd()))
                                    .and(QScoreLog.scoreLog.scorerNm.eq(scoreLog.getScorerNm()))
                                    .and(QScoreLog.scoreLog.logDttm.eq(scoreLog.getLogDttm()))
                                    .and(QScoreLog.scoreLog.virtNo.eq(scoreLog.getVirtNo()))
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

                    wrapper.getContent().forEach(examMap -> {
                        ExamMap tmp = examMapRepository.findOne(new BooleanBuilder()
                                .and(QExamMap.examMap.examinee.examineeCd.eq(examMap.getExaminee().getExamineeCd()))
                                //.and(qExamMap.hall.hallCd.eq(examMap.getHall().getHallCd())) // 고사실이 의미가 없는 경우 주석처리함
                                .and(QExamMap.examMap.exam.examCd.eq(examMap.getExam().getExamCd()))
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
