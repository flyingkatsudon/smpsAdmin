package com.humane.smps.controller.admin;

import com.humane.smps.dto.*;
import com.humane.smps.mapper.DataMapper;
import com.humane.smps.service.DataService;
import com.humane.smps.service.ImageService;
import com.humane.util.jasperreports.JasperReportsExportHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;


@RestController
@RequestMapping(value = "data", method = RequestMethod.GET)
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DataController {
    private static final String JSON = "json";
    private static final String COLMODEL = "colmodel";
    private static final String TXT = "txt";
    private final DataService dataService;
    private final DataMapper mapper;
    private final ImageService imageService;

    @RequestMapping(value = "examineeId.pdf")
    public ResponseEntity examineeId(ExamineeDto param, Pageable pageable) {
        List<ExamineeDto> list = mapper.examinee(param, pageable).getContent();

        list.forEach(item -> {
            try (InputStream is = imageService.getExaminee(item.getExamineeCd() + ".jpg")) {
                BufferedImage image;

                if (is == null) {
                    InputStream tmp = imageService.getExaminee("default.jpg");
                    image = ImageIO.read(tmp);
                } else image = ImageIO.read(is);

                item.setExamineeImage(image);
            } catch (IOException e) {
                log.error("{}", e.getMessage());
            }

            try (InputStream is = imageService.getUnivLogo("univLogo.png")) {
                BufferedImage image = ImageIO.read(is);
                item.setUnivLogo(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return JasperReportsExportHelper.toResponseEntity(
                "jrxml/examinee-id-card.jrxml"
                , JasperReportsExportHelper.EXT_PDF
                , list);
    }

    @RequestMapping(value = "examinee.{format:colmodel|json|pdf|xls|xlsx}")
    public ResponseEntity examinee(@PathVariable String format, ExamineeDto param, Pageable pageable) throws DRException {
        switch (format) {
            case COLMODEL:
                return ResponseEntity.ok(dataService.getExamineeModel());
            case JSON:
                return ResponseEntity.ok(mapper.examinee(param, pageable));
            default:
                JasperReportBuilder report = dataService.getExamineeReport();
                report.setDataSource(mapper.examinee(param, new PageRequest(0, Integer.MAX_VALUE, pageable.getSort())).getContent()); // 원인

                JasperPrint jasperPrint = report.toJasperPrint();
                jasperPrint.setName("수험생별 종합");

                return JasperReportsExportHelper.toResponseEntity(jasperPrint, format);
        }
    }

    @RequestMapping(value = "virtNo.{format:json|xls|xlsx}")
    public ResponseEntity virtNo(@PathVariable String format, ScoreDto param, Pageable pageable) throws ClassNotFoundException, JRException, DRException {
        switch (format) {
            case JSON:
                return ResponseEntity.ok(mapper.examMap(param, pageable));
            default:
                JasperReportBuilder report = dataService.getVirtNoReport();
                report.setDataSource(mapper.examMap(param, new PageRequest(0, Integer.MAX_VALUE, pageable.getSort())).getContent());

                JasperPrint jasperPrint = report.toJasperPrint();
                jasperPrint.setName("가번호 배정 현황");

                return JasperReportsExportHelper.toResponseEntity(jasperPrint, format);
        }
    }

    @RequestMapping(value = "scorerH.{format:colmodel|json|xls|xlsx}")
    public ResponseEntity scorerH(@PathVariable String format, ScoreDto param, Pageable pageable) throws DRException, JRException {
        try {
            switch (format) {
                case COLMODEL:
                    return ResponseEntity.ok(dataService.getScorerHModel());
                case JSON:
                    return ResponseEntity.ok(dataService.getScorerHData(param, pageable));
                default:
                    JasperReportBuilder report = dataService.getScorerHReport();
                    report.setDataSource(dataService.getScorerHData(param, new PageRequest(0, Integer.MAX_VALUE, pageable.getSort())).getContent());

                    JasperPrint jasperPrint = report.toJasperPrint();
                    jasperPrint.setName("채점자별 상세(가로)");

                    return JasperReportsExportHelper.toResponseEntity(jasperPrint, format);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // test
    @RequestMapping(value = "draw.{format:colmodel|json|xls|xlsx}")
    public ResponseEntity draw(@PathVariable String format, ScoreDto param, Pageable pageable) throws DRException, JRException {
        switch (format) {
            case COLMODEL:
                return ResponseEntity.ok(dataService.getDrawModel());
            case JSON:
                return ResponseEntity.ok(dataService.getDrawData(param, pageable));
            default:
                JasperReportBuilder report = dataService.getDrawReport();
                report.setDataSource(dataService.getDrawData(param, new PageRequest(0, Integer.MAX_VALUE, pageable.getSort())).getContent());

                JasperPrint jasperPrint = report.toJasperPrint();
                jasperPrint.setName("동점자 현황");

                return JasperReportsExportHelper.toResponseEntity(jasperPrint, format);
        }
    }

    @RequestMapping(value = "scorer.{format:colmodel|json|pdf|xls|xlsx}")
    public ResponseEntity scorer(@PathVariable String format, ScoreDto param, Pageable pageable) throws DRException, JRException {
        switch (format) {
            case COLMODEL:
                return ResponseEntity.ok(dataService.getScorerModel());
            case JSON:
                return ResponseEntity.ok(mapper.scorer(param, pageable));
            default:
                JasperReportBuilder report = dataService.getScorerReport();
                report.setDataSource(mapper.scorer(param, new PageRequest(0, Integer.MAX_VALUE, pageable.getSort())).getContent());

                JasperPrint jasperPrint = report.toJasperPrint();
                jasperPrint.setName("채점자별 상세(세로)");

                return JasperReportsExportHelper.toResponseEntity(jasperPrint, format);
        }
    }

    @RequestMapping(value = "attendance.{format:xlsx}")
    public ResponseEntity attendance(@PathVariable String format, ExamineeDto param, Pageable pageable) throws DRException, JRException {
        JasperReportBuilder report = dataService.attendanceReport();
        report.setDataSource(mapper.attendance(param, new PageRequest(0, Integer.MAX_VALUE, pageable.getSort())).getContent());

        JasperPrint jasperPrint = report.toJasperPrint();
        jasperPrint.setName("출결현황 리스트");

        return JasperReportsExportHelper.toResponseEntity(jasperPrint, format);
    }

    @RequestMapping(value = "scoreUpload.{format:xlsx}")
    public ResponseEntity scoreUpload(@PathVariable String format, ScoreUploadDto param, Pageable pageable) throws DRException, JRException {
        JasperReportBuilder report = dataService.getScoreUploadReport();
        report.setDataSource(mapper.scoreUpload(param, new PageRequest(0, Integer.MAX_VALUE, pageable.getSort())).getContent());

        JasperPrint jasperPrint = report.toJasperPrint();
        jasperPrint.setName("글로벌인재 성적업로드양식");

        return JasperReportsExportHelper.toResponseEntity(jasperPrint, format);
    }

    @RequestMapping(value = "failList.xlsx")
    public ResponseEntity failList(ExamineeDto param, Pageable pageable) throws DRException {
        return JasperReportsExportHelper.toResponseEntity(
                "jrxml/data-failList.jrxml"
                , JasperReportsExportHelper.EXT_XLSX
                , mapper.failList(param, new PageRequest(0, Integer.MAX_VALUE, pageable.getSort())).getContent());
    }

    @RequestMapping(value = "lawScoreUpload.{format:xlsx}")
    public ResponseEntity lawScoreUpload(@PathVariable String format, ScoreUploadDto param, Pageable pageable) throws DRException, JRException {
        JasperReportBuilder report = dataService.getScoreUploadReport();
        report.setDataSource(mapper.lawScoreUpload(param, new PageRequest(0, Integer.MAX_VALUE, pageable.getSort())).getContent());

        JasperPrint jasperPrint = report.toJasperPrint();
        jasperPrint.setName("법학서류평가 성적업로드양식");

        return JasperReportsExportHelper.toResponseEntity(jasperPrint, format);
    }

    @RequestMapping(value = "medScoreUpload.{format:xlsx}")
    public ResponseEntity medScoreUpload(@PathVariable String format, ScoreUploadDto param, Pageable pageable) throws DRException, JRException {
        JasperReportBuilder report = dataService.getScoreUploadReport();
        report.setDataSource(mapper.medScoreUpload(param, new PageRequest(0, Integer.MAX_VALUE, pageable.getSort())).getContent());

        JasperPrint jasperPrint = report.toJasperPrint();
        jasperPrint.setName("의대서류평가 성적업로드양식");

        return JasperReportsExportHelper.toResponseEntity(jasperPrint, format);
    }

    @RequestMapping(value = "knuScorer.{format:xlsx}")
    public ResponseEntity knuScorer(@PathVariable String format, ScoreDto param, Pageable pageable) throws DRException, JRException {
        JasperReportBuilder report = dataService.getKnuScorer();
        report.setDataSource(mapper.knuScorer(param, new PageRequest(0, Integer.MAX_VALUE, pageable.getSort())).getContent());

        JasperPrint jasperPrint = report.toJasperPrint();
        jasperPrint.setName("경북대학교 채점자별 상세(세로)");

        return JasperReportsExportHelper.toResponseEntity(jasperPrint, format);
    }

    @RequestMapping(value = "absentList.{format:xlsx}")
    public ResponseEntity absentList(@PathVariable String format, ExamineeDto param, Pageable pageable) throws DRException, JRException {
        JasperReportBuilder report = dataService.getAbsentList();
        report.setDataSource(mapper.absentList(param, new PageRequest(0, Integer.MAX_VALUE, pageable.getSort())).getContent());

        JasperPrint jasperPrint = report.toJasperPrint();
        jasperPrint.setName("경북대학교 결시자 리스트");

        return JasperReportsExportHelper.toResponseEntity(jasperPrint, format);
    }

    @RequestMapping(value = "physical.{format:json|colmodel|xlsx|txt}")
    public ResponseEntity physicalReport(@PathVariable String format, physicalDto param, Pageable pageable) throws DRException, JRException {
        switch (format) {
            case COLMODEL:
                return ResponseEntity.ok(dataService.getPhysicalModel());
            case JSON:
                return ResponseEntity.ok(mapper.physical(param, new PageRequest(0, Integer.MAX_VALUE, pageable.getSort())).getContent());
            case TXT:
                String userprofile = System.getenv("USERPROFILE");

                List<Map<String, String>> runningResult = mapper.runningResult();

                File file = new File(userprofile + "/Downloads/10m X 2회 왕복달리기_기록.txt");
                int increase = 1;
                while (file.exists()) {
                    increase++;
                    file = new File(userprofile + "/Downloads/10m X 2회 왕복달리기_기록(" + increase + ").txt");
                }
                if (!file.exists()) {
                    try {
                        //10m 기록 파일 생성
                        StringBuffer sb = new StringBuffer();

                        for (int i = 0; i < runningResult.size(); i++) {
                            Map map = runningResult.get(i);
                            if (map.get("total03") != null && !map.get("total03").equals("미응시") && !map.get("total03").equals("실격")) {
                                if (i == runningResult.size() - 1)
                                    sb.append(map.get("examineeCd") + "," + map.get("total03"));
                                else sb.append(map.get("examineeCd") + "," + map.get("total03") + ",");
                                sb.append(System.getProperty("line.separator"));
                            }
                        }
                        file.createNewFile();

                        BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));

                        bw.write(sb.toString());
                        bw.close();
                    } catch (IOException e) {
                    }
                }

                File file2 = new File(userprofile + "/Downloads/25m X 4회 왕복달리기_기록.txt");
                increase = 1;
                while (file2.exists()) {
                    increase++;
                    file2 = new File(userprofile + "/Downloads/25m X 4회 왕복달리기_기록(" + increase + ").txt");
                }
                if (!file2.exists()) {
                    try {
                        //10m 기록 파일 생성
                        StringBuffer sb2 = new StringBuffer();

                        for (int i = 0; i < runningResult.size(); i++) {
                            Map map = runningResult.get(i);
                            if (map.get("total04") != null && !map.get("total04").equals("미응시") && !map.get("total04").equals("실격")) {
                                if (i == runningResult.size() - 1)
                                    sb2.append(map.get("examineeCd") + "," + map.get("total04"));
                                else sb2.append(map.get("examineeCd") + "," + map.get("total04") + ",");
                                sb2.append(System.getProperty("line.separator"));
                            }
                        }
                        file2.createNewFile();

                        BufferedWriter bw2 = new BufferedWriter(new FileWriter(file2.getAbsoluteFile()));

                        bw2.write(sb2.toString());
                        bw2.close();
                    } catch (IOException e) {
                    }
                }
                return ResponseEntity.ok("텍스트파일 다운로드가 완료되었습니다");
            default:
                JasperReportBuilder report = dataService.getPhysicalReport();
                report.setDataSource(mapper.physical(param, new PageRequest(0, Integer.MAX_VALUE, pageable.getSort())).getContent());

                JasperPrint jasperPrint = report.toJasperPrint();
                jasperPrint.setName("한양대 에리카 체육 산출물");

                return JasperReportsExportHelper.toResponseEntity(jasperPrint, format);
        }
    }

    @RequestMapping(value = "skku.{format:colmodel|json|xls|xlsx}")
    public ResponseEntity skkuPeriod1(@PathVariable String format, ScoreDto param, Pageable pageable) throws DRException, JRException {
        try {
            switch (format) {
                case COLMODEL:
                    return ResponseEntity.ok(dataService.getScorerHModel());
                case JSON:
                    return ResponseEntity.ok(dataService.getScorerHData(param, pageable));
                default:
                    JasperReportBuilder report = dataService.getScorerHReport();
                    report.setDataSource(dataService.getSkkuPeriod1(param, new PageRequest(0, Integer.MAX_VALUE, pageable.getSort())).getContent());

                    JasperPrint jasperPrint = report.toJasperPrint();
                    jasperPrint.setName("채점자별 상세(가로)");

                    return JasperReportsExportHelper.toResponseEntity(jasperPrint, format);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // 초기에 시험이름, 시험코드를 불러옴
    @RequestMapping(value = "examInfo.json")
    public ResponseEntity examInfo() {
        try {
            List<ExamDto> examInfo = mapper.examInfo();
            log.debug("examInfo: {}", examInfo);

            return ResponseEntity.ok(mapper.examInfo());
        } catch (Exception e) {
            log.debug("{}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // 선택한 시험의 정보로 진행
    @RequestMapping(value = "fillVirtNo.json")
    public ResponseEntity fillVirtNo(String examCd) {
        try {
            // 1. 현재 마지막 가번호, 입력된 가번호 수, 선택한 시험의 학생 수 가져옴
         /*   ExamDto examDto = mapper.examDetail(examCd);
            log.debug("examDetail: {}", examDto);

            if (examDto.getAttendCnt() - examDto.getVirtNoCnt() == 0) {
                return ResponseEntity.ok("가번호가 모두 배정되어 있습니다.");
            } else {
                // 2. 남은 수험생 수 만큼 가번호 입력 (수험생 수 - 가번호 수)
                long cnt = examDto.getAttendCnt() - examDto.getVirtNoCnt();
                for (int i = 0; i < cnt; i++) {
                    examDto.setLastVirtNo(String.valueOf(Integer.parseInt(examDto.getLastVirtNo()) + 1));
                    mapper.fillVirtNo(examDto);
                }
                return ResponseEntity.ok("가번호가 입력되었습니다.");
            }*/

            // 임시 가번호 입력
            List<ExamDto> examDtoList = mapper.examDetail(examCd);

            // 고사실 갯수만큼 반복
            for (int i = 0; i < examDtoList.size(); i++) {
                ExamDto examDto = examDtoList.get(i);
                long cnt = examDto.getExamineeCnt();

                int virtNo = 0;
                if (examDto.getLastVirtNo() == null) virtNo = 1;
                else virtNo = Integer.parseInt(examDto.getLastVirtNo()) + 1;

                for (int j = virtNo; j <= cnt; j++) {
                    examDto.setLastVirtNo(String.valueOf(j));
                    mapper.fillVirtNo(examDto);
                }
            }
            return ResponseEntity.ok("가번호가 완료되었습니다.&nbsp;&nbsp;클릭하여 창을 종료하세요");
        } catch (Exception e) {
            log.debug("{}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("관리자에게 문의하세요");
        }
    }

    @RequestMapping(value = "fillEvalCd.json")
    public ResponseEntity fillEvalCd(String examCd) {
        try {
            // 1. paper_cd 정보 가져옴(수험번호, 답안지번호, 시험코드)
            EvalDto evalDto = new EvalDto();
            evalDto.setExamCd(examCd);
            List<EvalDto> evalList = mapper.paperToSmps(evalDto);

            // 2. eval_cd에 입력
            for (int i = 0; i < evalList.size(); i++)
                mapper.fillEvalCd(evalList.get(i));

            return ResponseEntity.ok("캔버스 번호가 저장되었습니다.&nbsp;&nbsp;클릭하여 창을 종료하세요");
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("답안지를 입력하는 시험이 아닙니다. 다른 시험을 선택하세요");
        }
    }

    @RequestMapping(value = "fillScore.json")
    public ResponseEntity fillScore(String examCd, String score) {
        try {
            // 1. 점수 채울 답안지 리스트 가져오기
            EvalDto evalDto = new EvalDto();
            evalDto.setExamCd(examCd);
            List<EvalDto> fillList = mapper.fillList(evalDto);

            // 2. 채점자 리스트 가져오기
            ScoreDto scoreDto = new ScoreDto();
            List<ScoreDto> scorerList = mapper.scorerList(scoreDto);

            // 3. 답안지 1개 당 X 채점자 수 만큼 점수 입력

            // 3-1. 어떤 평가위원의 점수도 전송되지 않았다면 return
            if (scorerList.size() == 0) {
                return ResponseEntity.ok("채점한 평가위원이 없습니다. 잠시 후 다시 시도하세요");
            } else {
                for (int i = 0; i < scorerList.size(); i++) {
                    for (int j = 0; j < fillList.size(); j++) {
                        // 3-1. 팝업에서 입력한 점수를 저장, 이미 저장되어 있으면 패스
                        fillList.get(j).setScore01(score);
                        fillList.get(j).setScorerNm(scorerList.get(i).getScorerNm());
                        mapper.fillScore(fillList.get(j));
                    }
                }
                return ResponseEntity.ok("점수가 입력되었습니다");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("관리자에게 문의하세요");
        }
    }

    @RequestMapping(value = "sqlEdit.{format:json|xls|xlsx}")
    public ResponseEntity sqlEdit(@PathVariable String format, @RequestParam(value = "sql") String sql) throws DRException {
        switch (format) {
            case JSON:
                return ResponseEntity.ok(mapper.sqlEdit(sql));
            default:
                List<Map<String, String>> list = mapper.sqlEdit(sql);

                for (Map<String, String> map : list) {
                    Set<String> keyset = map.keySet();
                    for (String key : keyset) {
                        Object value = map.get(key);
                        map.put(key, String.valueOf(value == null ? "" : String.valueOf(value)));
                    }
                }

                JasperReportBuilder report = report()
                        .setPageMargin(DynamicReports.margin(0))
                        .setIgnorePageWidth(true)
                        .setIgnorePagination(true);

                Set<String> keyset = list.get(0).keySet();
                for (String key : keyset) {
                    report.addColumn(
                            col.column(key, key, type.stringType())
                                    .setTitleStyle(DataService.columnHeaderStyle)
                                    .setStyle(DataService.columnStyle)
                                    .setFixedColumns(7)
                    );
                }

                report.setDataSource(list);
                JasperPrint jasperPrint = report.toJasperPrint();
                return JasperReportsExportHelper.toResponseEntity(jasperPrint, format);
        }
    }
}