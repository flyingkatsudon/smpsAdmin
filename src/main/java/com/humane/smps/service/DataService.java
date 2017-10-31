package com.humane.smps.service;

import com.humane.smps.dto.ExamDto;
import com.humane.smps.dto.ScoreDto;
import com.humane.smps.mapper.DataMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.VerticalTextAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.awt.Color.LIGHT_GRAY;
import static net.sf.dynamicreports.report.builder.DynamicReports.*;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DataService {
    // 산출물 디자인
    private StyleBuilder boldStyle = DynamicReports.stl.style().bold();
    private StyleBuilder boldCenteredStyle = DynamicReports.stl.style(boldStyle);

    private StyleBuilder columnTitleStyle = DynamicReports.stl.style(boldCenteredStyle)
            .setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE)
            .setPadding(2);

    public static StyleBuilder columnHeaderStyle = DynamicReports.stl.style()
            .setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE)
            .setBorder(DynamicReports.stl.penThin()).setBackgroundColor(LIGHT_GRAY);

    public static StyleBuilder columnStyle = DynamicReports.stl.style()
            .setBorder(DynamicReports.stl.penThin())
            .setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE);
    private final DataMapper mapper;

    @Data
    private class ColModel {
        private String name;
        private String label;
        private boolean sortable = true;

        public ColModel(String name, String label) {
            this.name = name;
            this.label = label;
        }

        public ColModel(String name, String label, boolean sortable) {
            this(name, label);
            this.sortable = sortable;
        }
    }

    public Object getExamineeModel() {
        // 기본 생성
        List<ColModel> colModels = new ArrayList<>();
        colModels.add(new ColModel("admissionNm", "전형"));
        colModels.add(new ColModel("typeNm", "계열"));
        colModels.add(new ColModel("examDate", "시험일자"));
        colModels.add(new ColModel("deptNm", "모집단위"));
        colModels.add(new ColModel("majorNm", "전공"));
        colModels.add(new ColModel("headNm", "고사본부"));
        colModels.add(new ColModel("bldgNm", "고사건물"));
        colModels.add(new ColModel("hallNm", "고사실"));
        colModels.add(new ColModel("virtNo", "가번호"));

        long itemCnt = mapper.getItemCnt();
        for (int i = 1; i <= itemCnt; i++) {
            /*colModels.add(new ColModel("avgScore" + (i < 10 ? "0" + i : i), "항목" + i + "평균"));*/
            colModels.add(new ColModel("totScore" + (i < 10 ? "0" + i : i), "항목" + i + "합계"));
        }
        colModels.add(new ColModel("scorerCnt", "평가위원수"));
        colModels.add(new ColModel("isAttend", "응시여부"));
        return colModels;
    }

    public List<ColModel> getScorerModel() {
        // 기본 생성
        List<ColModel> colModels = new ArrayList<>();
        colModels.add(new ColModel("admissionNm", "전형"));
        colModels.add(new ColModel("typeNm", "계열"));
        colModels.add(new ColModel("examDate", "시험일자"));
        colModels.add(new ColModel("deptNm", "모집단위"));
        colModels.add(new ColModel("majorNm", "전공"));
        /*colModels.add(new ColModel("headNm", "고사본부"));
        colModels.add(new ColModel("bldgNm", "고사건물"));
        colModels.add(new ColModel("hallNm", "고사실"));*/
        colModels.add(new ColModel("examineeCd", "수험번호"));
        colModels.add(new ColModel("examineeNm", "수험생명"));
        colModels.add(new ColModel("virtNo", "가번호"));
        colModels.add(new ColModel("groupNm", "조"));
        colModels.add(new ColModel("scorerNm", "평가위원"));

        long itemCnt = mapper.getItemCnt();

        for (int i = 1; i <= itemCnt; i++)
            colModels.add(new ColModel("score" + (i < 10 ? "0" + i : i), "항목" + i));

        colModels.add(new ColModel("totalScore", "총점"));
        colModels.add(new ColModel("scoreDttm", "채점시간"));

        return colModels;
    }

    public List<ColModel> getScorerHModel() {
        // 기본 생성
        List<ColModel> colModels = new ArrayList<>();
        colModels.add(new ColModel("admissionNm", "전형"));
        colModels.add(new ColModel("typeNm", "계열"));
        colModels.add(new ColModel("examDate", "시험일자"));
        colModels.add(new ColModel("deptNm", "모집단위"));
        colModels.add(new ColModel("majorNm", "전공"));
        /*colModels.add(new ColModel("headNm", "고사본부"));
        colModels.add(new ColModel("bldgNm", "고사건물"));
        colModels.add(new ColModel("hallNm", "고사실"));*/
        colModels.add(new ColModel("examineeCd", "수험번호"));
        colModels.add(new ColModel("examineeNm", "수험생명"));
        colModels.add(new ColModel("virtNo", "가번호"));
        colModels.add(new ColModel("groupNm", "조"));

        long scorerCnt = mapper.getScorerCnt(); // 채점자수
        long itemCnt = mapper.getItemCnt(); // 항목수

        colModels.add(new ColModel("total", "전체 총점"));
        for (int i = 1; i <= scorerCnt; i++) {
            colModels.add(new ColModel("scorerNm" + i, "평가위원" + i));
            for (int j = 1; j <= itemCnt; j++)
                colModels.add(new ColModel("score" + i + "S" + j, "항목" + i + "." + j));

            colModels.add(new ColModel("totalScore" + i, "총점" + i));
            /*colModels.add(new ColModel("scoreDttm" + i, "채점시간" + i, false));*/
        }
        return colModels;
    }

    public List<ColModel> getDrawModel() {
        // 기본 생성
        List<ColModel> colModels = new ArrayList<>();
        colModels.add(new ColModel("admissionNm", "전형"));
        colModels.add(new ColModel("typeNm", "계열"));
        colModels.add(new ColModel("examDate", "시험일자"));
        colModels.add(new ColModel("deptNm", "모집단위"));
        colModels.add(new ColModel("majorNm", "전공"));
        colModels.add(new ColModel("examineeCd", "수험번호"));
        colModels.add(new ColModel("examineeNm", "수험생명"));
        colModels.add(new ColModel("virtNo", "가번호"));
        /*colModels.add(new ColModel("evalCd", "답안지번호"));*/
        colModels.add(new ColModel("total", "전체 총점"));
        colModels.add(new ColModel("rank", "순위"));
        colModels.add(new ColModel("cnt", "동점자"));

        long scorerCnt = mapper.getScorerCnt(); // 채점자수
        long itemCnt = mapper.getItemCnt(); // 항목수

        for (int i = 1; i <= scorerCnt; i++) {
            colModels.add(new ColModel("scorerNm" + i, "평가위원" + i, false));
            for (int j = 1; j <= itemCnt; j++) colModels.add(new ColModel("score" + i + "S" + j, "항목" + j, false));

            colModels.add(new ColModel("totalScore" + i, "총점" + i, false));
        }
        return colModels;
    }

    // 한양대 에리카 체육
    public List<ColModel> getPhysicalModel() {
        // 기본 생성
        List<ColModel> colModels = new ArrayList<>();
        colModels.add(new ColModel("admissionNm", "전형"));
        colModels.add(new ColModel("typeNm", "계열"));
        colModels.add(new ColModel("examDate", "시험일자"));
        colModels.add(new ColModel("deptNm", "모집단위"));
        colModels.add(new ColModel("majorNm", "전공"));
        colModels.add(new ColModel("headNm", "고사본부"));
        colModels.add(new ColModel("bldgNm", "고사건물"));
        colModels.add(new ColModel("hallNm", "고사실"));
        colModels.add(new ColModel("examineeCd", "수험번호"));
        colModels.add(new ColModel("examineeNm", "수험생명"));
        colModels.add(new ColModel("virtNo", "실기번호"));
        colModels.add(new ColModel("groupNm", "조"));

        List<ExamDto> examDto = mapper.ericaExamInfo("802%");

        for (int i = 0; i < examDto.size(); i++) {
            long itemCnt = mapper.ericaItemCnt(examDto.get(i).getExamCd());

            colModels.add(new ColModel("scorerNm" + (i+1) + "1", "측정교수"));
            colModels.add(new ColModel("scorerNm" + (i+1) + "2", "기록입력위원"));

            for (int j = 1; j <= itemCnt; j++) {
                if (itemCnt < 2) continue;
                else colModels.add(new ColModel("score" + (i+1) + j, examDto.get(i).examNm + " 측정기록"));
            }
            colModels.add(new ColModel("total" + ((i+1) < 10 ? "0" + (i+1) : (i+1)), examDto.get(i).examNm + " 최고기록"));
            colModels.add(new ColModel("scoreDttm" + (i+1), "채점시간"));
            /*colModels.add(new ColModel("grade" + ((i+1) < 10 ? "0" + (i+1) : (i+1)), "변환점수"));*/
        }

        return colModels;
    }

    public JasperReportBuilder getExamineeReport() {
        JasperReportBuilder report = report()
                .title(cmp.text("수험생별 종합").setStyle(columnTitleStyle))
                .columns(
                        col.reportRowNumberColumn("번호").setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        col.column("전형", "admissionNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(13),
                        col.column("계열", "typeNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        col.column("시험일자", "examDate", type.dateType()).setPattern("yyyy-MM-dd").setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(8),
                        col.column("모집단위", "deptNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        col.column("전공", "majorNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        /*col.column("고사본부", "headNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        col.column("고사건물", "bldgNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        col.column("고사실", "hallNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),*/
                        col.column("가번호", "virtNo", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7)
                )
                .setPageMargin(DynamicReports.margin(0))
                .setIgnorePageWidth(true)
                .setIgnorePagination(true);

        long itemCnt = mapper.getItemCnt();

        for (int i = 1; i <= itemCnt; i++) {
            /*report.addColumn(col.column("항목" + i + "평균", "avgScore" + (i < 10 ? "0" + i : i), type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7));*/
            report.addColumn(col.column("항목" + i + "합계", "totScore" + (i < 10 ? "0" + i : i), type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7));
        }
        report.addColumn(col.column("평가위원수", "scorerCnt", type.longType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7));
        report.addColumn(col.column("응시여부", "isAttend", type.booleanType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7));

        return report;

    }

    public JasperReportBuilder getVirtNoReport() {
        JasperReportBuilder report = report()
                .title(cmp.text("가번호 배정 현황").setStyle(columnTitleStyle))
                .columns(
                        col.reportRowNumberColumn("번호").setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        col.column("가번호", "virtNo", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        /*col.column("답안지번호", "evalCd", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),*/
                        col.column("조", "groupNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        col.column("수험번호", "examineeCd", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        /*col.column("수험생명", "examineeNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),*/
                        col.column("전형", "admissionNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(13),
                        col.column("계열", "typeNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        col.column("시험일자", "examDate", type.dateType()).setPattern("yyyy-MM-dd").setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(8),
                        col.column("모집단위", "deptNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(8),
                        col.column("전공", "majorNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7)
                        /*col.column("고사본부", "headNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(9),
                        col.column("고사건물", "bldgNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        col.column("고사실", "hallNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7)*/
                )
                .setPageMargin(DynamicReports.margin(0))
                .setIgnorePageWidth(true)
                .setIgnorePagination(true);
        return report;
    }

    public JasperReportBuilder getDrawReport() {
        JasperReportBuilder report = report()
                .title(cmp.text("동점자 현황").setStyle(columnTitleStyle))
                .columns(
                        col.reportRowNumberColumn("번호").setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(4),
                        col.column("전형", "admissionNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(13),
                        col.column("계열", "typeNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        col.column("시험일자", "examDate", type.dateType()).setPattern("yyyy-MM-dd").setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(8),
                        col.column("모집단위", "deptNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(8),
                        col.column("전공", "majorNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(8),
                        col.column("수험번호", "examineeCd", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        col.column("수험생명", "examineeNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        col.column("가번호", "virtNo", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        col.column("답안지번호", "evalCd", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        col.column("전체 총점", "total", type.doubleType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        col.column("순위", "rank", type.longType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(4),
                        col.column("동점자", "cnt", type.longType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(4)
                )
                .setPageMargin(DynamicReports.margin(0))
                .setIgnorePageWidth(true)
                .setIgnorePagination(true);

        long scorerCnt = mapper.getScorerCnt();
        long itemCnt = mapper.getItemCnt();

        for (int i = 1; i <= scorerCnt; i++) {
            report.addColumn(col.column("평가위원", "scorerNm" + i, type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7));
            for (int j = 1; j <= itemCnt; j++)
                report.addColumn(col.column("항목" + j, "score" + i + "S" + j, type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7));
            report.addColumn(col.column("총점" + i, "totalScore" + i, type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7));
        }

        return report;
    }

    public JasperReportBuilder getScorerHReport() {
        JasperReportBuilder report = report()
                .title(cmp.text("채점자별 상세(가로)").setStyle(columnTitleStyle))
                .columns(
                        col.reportRowNumberColumn("번호").setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        col.column("전형", "admissionNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(13),
                        col.column("계열", "typeNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        col.column("시험일자", "examDate", type.dateType()).setPattern("yyyy-MM-dd").setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(8),
                        col.column("모집단위", "deptNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(11),
                        col.column("전공", "majorNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(11),
                        col.column("수험번호", "examineeCd", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        col.column("수험생명", "examineeNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        col.column("가번호", "virtNo", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        col.column("전체 총점", "total", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        /*col.column("답안지번호", "evalCd", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),*/
                        col.column("조", "groupNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7)
                )
                .setPageMargin(DynamicReports.margin(0))
                .setIgnorePageWidth(true)
                .setIgnorePagination(true);

        long scorerCnt = mapper.getScorerCnt();
        long itemCnt = mapper.getItemCnt();

        for (int i = 1; i <= scorerCnt; i++) {
            report.addColumn(col.column("평가위원" + i, "scorerNm" + i, type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7));
            for (int j = 1; j <= itemCnt; j++)
                report.addColumn(col.column("항목" + i + "." + j, "score" + i + "S" + j, type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7));
            // TODO: 법전과 타 전형 간 '총점' 컬럼 구분 필요
            // 1. 법전
            //report.addColumn(col.column("총점" + i, "totalScore" + i, type.doubleType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7));
            // 2. 그 외
            report.addColumn(col.column("총점" + i, "totalScore" + i, type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7));
        }
        return report;
    }

    // TODO: 일반 세로 산출물
    public JasperReportBuilder getScorerReport() {
        JasperReportBuilder report = report()
                .title(cmp.text("채점자별 상세(세로)").setStyle(columnTitleStyle))
                .columns(
                        col.reportRowNumberColumn("번호").setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        col.column("전형", "admissionNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(13),
                        col.column("계열", "typeNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        col.column("시험일자", "examDate", type.dateType()).setPattern("yyyy-MM-dd").setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(8),
                        col.column("모집단위", "deptNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(11),
                        col.column("전공", "majorNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(11),
                        col.column("수험번호", "examineeCd", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        col.column("수험생명", "examineeNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        col.column("가번호", "virtNo", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        /*col.column("답안지번호", "evalCd", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),*/
                        col.column("조", "groupNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        col.column("평가위원", "scorerNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7)
                )
                .setPageMargin(DynamicReports.margin(0))
                .setIgnorePageWidth(true)
                .setIgnorePagination(true);

        long itemCnt = mapper.getItemCnt();
        for (int i = 1; i <= itemCnt; i++)
            report.addColumn(col.column("항목" + i, "score" + (i < 10 ? "0" + i : i), type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7));

        //report.addColumn(col.column("총점", "totalScore", type.bigDecimalType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7));// 법대용
        report.addColumn(col.column("총점", "totalScore", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7));
        return report;

    }

    // TODO: 경북대용 세로 산출물
    public JasperReportBuilder getKnuScorer() {
        JasperReportBuilder report = report()
                .title(cmp.text("채점자별 상세(세로)").setStyle(columnTitleStyle))
                .columns(
                        col.reportRowNumberColumn("번호").setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(3),
                        col.column("전형", "admissionNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(4),
                        col.column("수험번호", "examineeCd", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        col.column("전형구분", "exmAdmNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(16),
                        col.column("모집단위", "deptNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(16),
                        col.column("수험생명", "examineeNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        col.column("생년월일", "birth", type.dateType()).setPattern("yyyy-MM-dd").setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        col.column("고사건물", "bldgNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(10),
                        col.column("고사실", "hallNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        col.column("평가위원", "scorerNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7)
                )
                .setPageMargin(DynamicReports.margin(0))
                .setIgnorePageWidth(true)
                .setIgnorePagination(true);

        long itemCnt = mapper.getItemCnt();
        for (int i = 1; i <= itemCnt; i++) {
            report.addColumn(col.column("항목" + i, "score" + (i < 10 ? "0" + i : i), type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7));
            report.addColumn(col.column("점수" + i, "grade" + (i < 10 ? "0" + i : i), type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7));
        }
        report.addColumn(col.column("총점", "totalScore", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7));

        return report;
    }

    public JasperReportBuilder attendanceReport() {
        JasperReportBuilder report = report()
                .title(cmp.text("출결현황 리스트").setStyle(columnTitleStyle))
                .columns(
                        col.reportRowNumberColumn("번호").setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(3),
                        col.column("전형", "admissionNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(15),
                        col.column("시험일자", "examDate", type.dateType()).setPattern("yyyy-MM-dd").setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(8),
                        col.column("전공", "majorNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        col.column("수험번호", "examineeCd", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        col.column("수험생명", "examineeNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        col.column("실기번호", "virtNo", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        col.column("조", "groupNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(3),
                        col.column("응시여부", "attendance", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7))
                .setPageMargin(DynamicReports.margin(0))
                .setIgnorePageWidth(true)
                .setIgnorePagination(true);
        return report;
    }

    // 경북대용
    public JasperReportBuilder getAbsentList() {
        JasperReportBuilder report = report()
                .columns(
                        col.reportRowNumberColumn("번호").setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(3),
                        col.column("모집단위", "deptNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(16),
                        col.column("전형구분", "exmAdmNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(16),
                        col.column("수험번호", "examineeCd", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        col.column("수험생명", "examineeNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7)
                )
                .setPageMargin(DynamicReports.margin(0))
                .setIgnorePageWidth(true)
                .setIgnorePagination(true);

        return report;
    }

    public JasperReportBuilder getScoreUploadReport() {
        JasperReportBuilder report = report()
                .columns(
                        col.column("입학년도", "year", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        col.column("수험번호", "examineeCd", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        col.column("결시여부", "isAttend", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        col.column("평가위원", "scorerCd", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        col.column("항목코드", "itemNo", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        col.column("평가값", "score", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7)
                )
                .setPageMargin(DynamicReports.margin(0))
                .setIgnorePageWidth(true)
                .setIgnorePagination(true);

        return report;
    }

    public JasperReportBuilder getPhysicalReport() {
        List<ExamDto> examDto = mapper.ericaExamInfo("802%");

        JasperReportBuilder report = report()
                .title(cmp.text("한양대 에리카 체육 산출물").setStyle(columnTitleStyle))
                .columns(
                        col.reportRowNumberColumn("번호").setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(3),
                        col.column("전형", "admissionNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(15),
                        col.column("계열", "typeNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(5),
                        col.column("시험일자", "examDate", type.dateType()).setPattern("yyyy-MM-dd").setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(8),
                        col.column("모집단위", "deptNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        col.column("전공", "majorNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        col.column("고사본부", "headNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        col.column("고사건물", "bldgNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        col.column("고사실", "hallNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(10),
                        col.column("수험번호", "examineeCd", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        col.column("수험생명", "examineeNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        col.column("실기번호", "virtNo", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        col.column("조", "groupNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(3)
                )
                .setPageMargin(DynamicReports.margin(0))
                .setIgnorePageWidth(true)
                .setIgnorePagination(true);

        log.debug("{}", examDto);
        for (int i = 0; i < examDto.size(); i++) {
            long itemCnt = mapper.ericaItemCnt(examDto.get(i).getExamCd());

            report.addColumn(col.column("측정교수", "scorerNm" + (i+1) + "1", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(5));
            report.addColumn(col.column("기록입력위원", "scorerNm" + (i+1) + "2", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7));

            for (int j = 1; j <= itemCnt; j++) {
                if (itemCnt < 2) continue;
                else report.addColumn(col.column(examDto.get(i).examNm + " 측정기록" + j, "score" + (i+1) + j, type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(15));
            }
            report.addColumn(col.column(examDto.get(i).examNm + " 최고기록", "total" + ((i+1) < 10 ? "0" + (i+1) : (i+1)), type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(17));
            report.addColumn(col.column("채점시간", "scoreDttm" + (i+1), type.dateType()).setPattern("yyyy-MM-dd HH:mm:ss").setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(12));
            /*report.addColumn(col.column("변환점수", "grade" + ((i+1) < 10 ? "0" + (i+1) : (i+1)), type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(12));*/
        }
        return report;
    }

    // TODO: 기본형, 결시생은 param.getAbsentValue()로 처리하는 경우, ex) 한양대 법대: 'F', 경북대: '결시' 등
    /*public Page<Map<String, Object>> getScorerHData(ScoreDto param, Pageable pageable) {
        Page<Map<String, Object>> page = mapper.examMap(param, pageable);
        return fillMap(param.getAbsentValue(), page);
    }*/

    private Page<Map<String, Object>> fillMap(String absentValue, Page<Map<String, Object>> page) {
        page.forEach(map -> {
            String examCd = map.get("examCd") == null ? null : map.get("examCd").toString();
            String virtNo = map.get("virtNo") == null ? null : map.get("virtNo").toString();
            if (examCd != null && virtNo != null) {
                List<Map<String, Object>> scoreList = mapper.scorerH(map);
                double total = 0;
                for (int i = 1; i <= scoreList.size(); i++) {
                    Map<String, Object> score = scoreList.get(i - 1);
                    if (score != null) {
                        map.put("SCORER_NM" + i, score.get("scorerNm"));
                        map.put("SCORE" + i + "_S1", score.get("score01"));
                        map.put("SCORE" + i + "_S2", score.get("score02"));
                        map.put("SCORE" + i + "_S3", score.get("score03"));
                        map.put("SCORE" + i + "_S4", score.get("score04"));
                        map.put("SCORE" + i + "_S5", score.get("score05"));
                        map.put("SCORE" + i + "_S6", score.get("score06"));
                        map.put("SCORE" + i + "_S7", score.get("score07"));
                        map.put("SCORE" + i + "_S8", score.get("score08"));
                        map.put("SCORE" + i + "_S9", score.get("score09"));
                        map.put("SCORE" + i + "_S10", score.get("score10"));
                        map.put("TOTAL_SCORE" + i, score.get("totalScore"));
                        map.put("SCORE_DTTM" + i, score.get("scoreDttm"));

                        String tmp = String.valueOf(score.get("totalScore"));

                        if(!tmp.equals(absentValue)){
                            total += Double.parseDouble(tmp);
                        }
                    }
                }

                if(absentValue.equals("F")) map.put("TOTAL", String.format("%.1f", total));
                else map.put("TOTAL", total);
            }
        });

        return page;
    }

    // TODO: 일반 가로버전, fillMap 없이 하나의 쿼리로
    public Page<Map<String, Object>> getScorerHData(ScoreDto param, Pageable pageable) {
        return mapper.scoredH(param, pageable);
    }

    // 가로버전
    public Page<Map<String, Object>> getSkkuPeriod1(ScoreDto param, Pageable pageable) {
        return mapper.skkuPeriod1(param, pageable);
    }

    // 동점자 현황
    public Page<Map<String, Object>> getDrawData(ScoreDto param, Pageable pageable) {
        Page<Map<String, Object>> page = mapper.drawData(param, pageable);
        return fillMap(param.getAbsentValue(), page);
        // return mapper.drawData(param, pageable);
    }
}
