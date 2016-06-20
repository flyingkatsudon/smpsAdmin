package com.humane.smps.controller.admin;

import com.humane.smps.dto.ExamineeDto;
import com.humane.smps.dto.ScoreDto;
import com.humane.smps.mapper.DataMapper;
import com.humane.smps.service.ImageService;
import com.humane.util.jasperreports.JasperReportsExportHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private static final String CHART = "chart";
    private static final String JSON = "json";
    private final DataMapper mapper;
    private final ImageService imageService;

    @RequestMapping(value = "examinee.{format:json|chart|pdf|xls|xlsx}")
    public ResponseEntity examinee(@PathVariable String format, ExamineeDto param, Pageable pageable) {
        switch (format) {
            case JSON:
                return ResponseEntity.ok(mapper.examinee(param, pageable));
            case CHART:
                return ResponseEntity.ok(mapper.examinee(param, pageable).getContent());
            default:
                return JasperReportsExportHelper.toResponseEntity(
                        "jrxml/data-examinee.jrxml"
                        , format
                        , mapper.examinee(param, pageable).getContent());
        }
    }

    @RequestMapping(value = "scorer.{format:json|chart|pdf|xls|xlsx}")
    public ResponseEntity scorer(@PathVariable String format, ScoreDto param, Pageable pageable) {
        switch (format) {
            case JSON:
                return ResponseEntity.ok(mapper.score(param, pageable));
            case CHART:
                return ResponseEntity.ok(mapper.score(param, pageable).getContent());
            default:
                return JasperReportsExportHelper.toResponseEntity(
                        "jrxml/data-scorer.jrxml"
                        , format
                        , mapper.score(param, pageable).getContent());
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
                // "jrxml/examinee-id-card.jrxml"
                "jrxml/idCard.jrxml"
                , JasperReportsExportHelper.EXT_PDF
                , list);
    }
}