package com.humane.smps.controller.admin;

import com.humane.smps.dto.ExamineeDto;
import com.humane.smps.dto.ScoreDto;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

@RestController
@RequestMapping(value = "data", method = RequestMethod.GET)
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DataController {
    private static final String JSON = "json";
    private static final String COLMODEL = "colmodel";
    private final DataService dataService;
    private final DataMapper mapper;
    private final ImageService imageService;

    @RequestMapping(value = "examinee.{format:colmodel|json|pdf|xls|xlsx}")
    public ResponseEntity examinee(@PathVariable String format, ExamineeDto param, Pageable pageable) {
        switch (format) {
            case COLMODEL:
                return ResponseEntity.ok(dataService.getExamineeModel());
            case JSON:
                return ResponseEntity.ok(mapper.examinee(param, pageable));
            default:
                return JasperReportsExportHelper.toResponseEntity(
                        "jrxml/data-examinee.jrxml"
                        , format
                        , mapper.examinee(param, pageable).getContent());
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
                JasperReportBuilder report = report()
                        .title(cmp.text("채점자별 상세(세로)"))
                        .columns(
                                col.column("전형", "admissionNm", type.stringType()),
                                col.column("시험일자", "examDate", type.dateType()).setPattern("yyyy-MM-dd"),
                                col.column("시험시간", "examTime", type.dateType()).setPattern("HH:mm:ss"),
                                col.column("모집단위", "deptNm", type.stringType()),
                                col.column("전공", "majorNm", type.stringType()),
                                col.column("수험번호", "examineeCd", type.stringType()),
                                col.column("수험생명", "examineeNm", type.stringType()),
                                col.column("가번호", "virtNo", type.stringType()),
                                col.column("조", "groupNm", type.stringType()),
                                col.column("평가위원", "scorerNm", type.stringType())
                        )
                        .setDataSource(mapper.scorer(param, pageable).getContent())
                        .setPageMargin(DynamicReports.margin(0))
                        .setIgnorePageWidth(true)
                        .setIgnorePagination(true);

                long itemCnt = mapper.getItemCnt();
                for (int i = 1; i <= itemCnt; i++)
                    report.addColumn(col.column("항목" + i, "score" + (i < 10 ? "0" + i : i), type.stringType()));

                report.addColumn(col.column("총점", "totalScore", type.stringType()));
                report.addColumn(col.column("메모", "memo", type.stringType()));
                report.addColumn(col.column("사진", "isPhoto", type.stringType()));
                report.addColumn(col.column("채점시간", "scoreDttm", type.dateType()).setPattern("yyyy-MM-dd HH:mm:ss"));

                JasperPrint jasperPrint = report.toJasperPrint();
                jasperPrint.setName("채점자별 상세(세로)");

                return JasperReportsExportHelper.toResponseEntity(jasperPrint, format);
        }
    }

    @RequestMapping(value = "examineeId.pdf")
    public ResponseEntity examineeId(ExamineeDto param, Pageable pageable) {
        List<ExamineeDto> list = mapper.examinee(param, pageable).getContent();
        list.forEach(item -> {
            try (InputStream is = imageService.getImageExaminee(item.getExamineeCd() + ".jpg")) {
                BufferedImage image = ImageIO.read(is);
                item.setExamineeImage(image);
            } catch (IOException e) {
                log.error("{}", e.getMessage());
            }
        });

        return JasperReportsExportHelper.toResponseEntity(
                "jrxml/examinee-id-card.jrxml"
                , JasperReportsExportHelper.EXT_PDF
                , list);
    }


    @RequestMapping(value = "scorerH.{format:colmodel|json|xls|xlsx}")
    public ResponseEntity scorerH(@PathVariable String format, ScoreDto param, Pageable pageable) throws DRException, JRException {
        switch (format) {
            case COLMODEL:
                return ResponseEntity.ok(dataService.getScorerHModel());
            case JSON:
                return ResponseEntity.ok(dataService.getScorerH(param, pageable));
            default:

                List<Map<String, Object>> list = dataService.getScorerH(param, pageable).getContent();

                JasperReportBuilder report = report()
                        .title(cmp.text("채점자별 상세(가로)"))
                        .columns(
                                col.column("전형", "admissionNm", type.stringType()),
                                col.column("시험일자", "examDate", type.dateType()).setPattern("yyyy-MM-dd"),
                                col.column("시험시간", "examTime", type.dateType()).setPattern("HH:mm:ss"),
                                col.column("모집단위", "deptNm", type.stringType()),
                                col.column("전공", "majorNm", type.stringType()),
                                col.column("수험번호", "examineeCd", type.stringType()),
                                col.column("수험생명", "examineeNm", type.stringType()),
                                col.column("가번호", "virtNo", type.stringType()),
                                col.column("조", "groupNm", type.stringType())
                        )
                        .setDataSource(list)
                        .setPageMargin(DynamicReports.margin(0))
                        .setIgnorePageWidth(true)
                        .setIgnorePagination(true);

                long scorerCnt = mapper.getScorerCnt();
                long itemCnt = mapper.getItemCnt();

                for (int i = 1; i <= scorerCnt; i++) {
                    report.addColumn(col.column("평가위원", "scorerNm" + i, type.stringType()));
                    for (int j = 1; j <= itemCnt; j++)
                        report.addColumn(col.column("항목" + j, "score" + i + "S" + j, type.stringType()));
                    report.addColumn(col.column("채점시간", "scoreDttm", type.stringType()));
                }

                JasperPrint jasperPrint = report.toJasperPrint();
                jasperPrint.setName("채점자별 상세(가로)");

                return JasperReportsExportHelper.toResponseEntity(jasperPrint, format);
        }
    }

    @RequestMapping(value = "virtNo.{format:colmodel|json|xls|xlsx}")
    public ResponseEntity virtNo(@PathVariable String format, ScoreDto param, Pageable pageable) throws ClassNotFoundException, JRException, DRException {
        switch (format) {
            case COLMODEL:
                return ResponseEntity.ok(mapper.examMap(param, pageable));
            case JSON:
                return ResponseEntity.ok(mapper.examMap(param, pageable));
            default:
                JasperReportBuilder report = report()
                        .title(cmp.text("가번호 배정 현황"))
                        .columns(
                                col.column("가번호", "virtNo", type.stringType()),
                                col.column("조", "groupNm", type.stringType()),
                                col.column("수험번호", "examineeCd", type.stringType()),
                                col.column("수험생명", "examineeNm", type.stringType()),
                                col.column("전형", "admissionNm", type.stringType()),
                                col.column("시험일자", "examDate", type.dateType()).setPattern("yyyy-MM-dd"),
                                col.column("시험시간", "examTime", type.dateType()).setPattern("HH:mm:ss"),
                                col.column("모집단위", "deptNm", type.stringType()),
                                col.column("전공", "majorNm", type.stringType()),
                                col.column("고사본부", "headNm", type.stringType()),
                                col.column("고사건물", "bldgNm", type.stringType()),
                                col.column("고사실", "hallNm", type.stringType())
                        )
                        .setDataSource(mapper.examMap(param, pageable).getContent())
                        .setPageMargin(DynamicReports.margin(0))
                        .setIgnorePageWidth(true)
                        .setIgnorePagination(true);

                JasperPrint jasperPrint = report.toJasperPrint();
                jasperPrint.setName("가번호 배정 현황");

                return JasperReportsExportHelper.toResponseEntity(jasperPrint, format);
        }
    }
}