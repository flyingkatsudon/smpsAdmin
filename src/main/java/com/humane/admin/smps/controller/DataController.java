package com.humane.admin.smps.controller;

import com.humane.admin.smps.dto.ExamineeDto;
import com.humane.admin.smps.dto.ScoreDto;
import com.humane.admin.smps.service.ApiService;
import com.humane.admin.smps.service.ImageService;
import com.humane.util.ObjectConvert;
import com.humane.util.jasperreports.JasperReportsExportHelper;
import com.humane.util.spring.PageRequest;
import com.humane.util.spring.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import retrofit2.Response;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping(value = "data")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DataController {
    private final ApiService apiService;
    private final ImageService imageService;
    private static final String LIST = "list";

    @RequestMapping(value = "examinee/{format:list|pdf|xls|xlsx}")
    public ResponseEntity examinee(@PathVariable String format, ExamineeDto examineeDto, PageRequest pager, HttpServletResponse response) {
        switch (format) {
            case LIST:
                Response<PageResponse<ExamineeDto>> pageResponse = apiService.examinee(
                        ObjectConvert.asMap(examineeDto),
                        pager.getPage(),
                        pager.getSize(),
                        pager.getSort());
                return ResponseEntity.ok(pageResponse.body());
            default:
                List<ExamineeDto> list = apiService.examinee(ObjectConvert.asMap(examineeDto), pager.getSort());
                return JasperReportsExportHelper.toResponseEntity(
                        response,
                        "jrxml/data-examinee.jrxml",
                        format,
                        list
                );
        }
    }

    @RequestMapping(value = "examineeId/{format:pdf}")
    public ResponseEntity examineeId(@PathVariable String format, ExamineeDto examineeDto, PageRequest pager, HttpServletResponse response) {
        List<ExamineeDto> list = apiService.examinee(ObjectConvert.asMap(examineeDto), pager.getSort());

        list.forEach(item -> {
            try (InputStream is = imageService.getImageExaminee(item.getExamineeCd() + ".jpg")) {
                BufferedImage image = ImageIO.read(is);
                item.setExamineeImage(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return JasperReportsExportHelper.toResponseEntity(
                response,
                "jrxml/examinee-id-card.jrxml",
                format,
                list
        );
    }

    @RequestMapping(value = "scorer/{format:list|pdf|xls|xlsx}")
    public ResponseEntity scorer(@PathVariable String format, ScoreDto scoreDto, PageRequest pager, HttpServletResponse response) {
        switch (format) {
            case LIST:
                Response<PageResponse<ScoreDto>> pageResponse = apiService.score(
                        ObjectConvert.asMap(scoreDto) ,
                        pager.getPage(),
                        pager.getSize(),
                        pager.getSort());
                return ResponseEntity.ok(pageResponse.body());
            default:
                List<ScoreDto> list = apiService.score(ObjectConvert.asMap(scoreDto), pager.getSort());
                return JasperReportsExportHelper.toResponseEntity(
                        response,
                        "jrxml/data-scorer.jrxml",
                        format,
                        list
                );
        }
    }

}