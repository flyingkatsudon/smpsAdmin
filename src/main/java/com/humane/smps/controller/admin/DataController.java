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


    @RequestMapping(value = "examineeId.pdf")
    public ResponseEntity examineeId(ExamineeDto param, Pageable pageable) {
        List<ExamineeDto> list = mapper.examinee(param, pageable).getContent();
        list.forEach(item -> {
            try (InputStream is = imageService.getExaminee(item.getExamineeCd() + ".jpg")) {
                BufferedImage image = ImageIO.read(is);
                item.setExamineeImage(image);
            } catch (IOException e) {
                log.error("{}", e.getMessage());
            }

            try (InputStream is = imageService.getUnivLogo("symbol_03.jpg")) {
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
                report.setDataSource(mapper.examinee(param, pageable).getContent());

                JasperPrint jasperPrint = report.toJasperPrint();
                jasperPrint.setName("수험생 별 종합");

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
                report.setDataSource(mapper.examMap(param, pageable).getContent());

                JasperPrint jasperPrint = report.toJasperPrint();
                jasperPrint.setName("가번호 배정 현황");

                return JasperReportsExportHelper.toResponseEntity(jasperPrint, format);
        }
    }

    @RequestMapping(value = "scorerH.{format:colmodel|json|xls|xlsx}")
    public ResponseEntity scorerH(@PathVariable String format, ScoreDto param, Pageable pageable) throws DRException, JRException {
        switch (format) {
            case COLMODEL:
                return ResponseEntity.ok(dataService.getScorerHModel());
            case JSON:
                return ResponseEntity.ok(dataService.getScorerHData(param, pageable));
            default:
                JasperReportBuilder report = dataService.getScorerHReport();
                report.setDataSource(dataService.getScorerHData(param, pageable).getContent());

                JasperPrint jasperPrint = report.toJasperPrint();
                jasperPrint.setName("채점자별 상세(가로)");

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
                report.setDataSource(mapper.scorer(param, pageable).getContent());

                JasperPrint jasperPrint = report.toJasperPrint();
                jasperPrint.setName("채점자별 상세(세로)");

                return JasperReportsExportHelper.toResponseEntity(jasperPrint, format);
        }
    }
}