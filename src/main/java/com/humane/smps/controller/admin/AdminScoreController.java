package com.humane.smps.controller.admin;

import com.humane.smps.dto.ScoreDto;
import com.humane.smps.dto.SheetDto;
import com.humane.smps.mapper.AdminScoreMapper;
import com.humane.smps.service.AdminScoreService;
import com.humane.util.jasperreports.JasperReportsExportHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@RestController
@RequestMapping(value = "score", method = RequestMethod.GET)
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdminScoreController {
    private static final String COLMODEL = "colmodel";
    private static final String JSON = "json";
    private static final String PDF = "pdf";
    private final AdminScoreMapper adminScoreMapper;
    private final AdminScoreService adminScoreService;

    @RequestMapping(value = "print.{format:json|pdf|xls|xlsx}")
    public ResponseEntity print(@PathVariable String format, SheetDto param, Pageable pageable) {
        switch (format) {
            case JSON:
                return ResponseEntity.ok(adminScoreMapper.sheet(param, pageable));
            default:
                return JasperReportsExportHelper.toResponseEntity(
                        "jrxml/score-print.jrxml"
                        , format
                        , adminScoreMapper.sheet(param, new PageRequest(0, Integer.MAX_VALUE, pageable.getSort())).getContent());
        }
    }

    @RequestMapping(value = "cancel.{format:json|pdf|xls|xlsx}")
    public ResponseEntity cancel(@PathVariable String format, SheetDto param, Pageable pageable) {
        param.setIsCancel(true);
        switch (format) {
            case JSON:
                return ResponseEntity.ok(adminScoreMapper.sheet(param, pageable));
            default:
                return JasperReportsExportHelper.toResponseEntity(
                        "jrxml/score-cancel.jrxml"
                        , format
                        , adminScoreMapper.sheet(param, new PageRequest(0, Integer.MAX_VALUE, pageable.getSort())).getContent());
        }
    }

    @RequestMapping(value = "fix.{format:json|pdf|xls|xlsx}")
    public ResponseEntity fix(@PathVariable String format, ScoreDto param, Pageable pageable) {
        switch (format) {
            case JSON:
                return ResponseEntity.ok(adminScoreMapper.fix(param, pageable));
            default:
                return JasperReportsExportHelper.toResponseEntity(
                        "jrxml/score-fix.jrxml"
                        , format
                        , adminScoreMapper.fix(param, new PageRequest(0, Integer.MAX_VALUE, pageable.getSort())).getContent());
        }
    }
    @Value("${path.smps.pdf:C:/api/smps/pdf}") String path;

    @RequestMapping(value = "detail.pdf")
    public ResponseEntity detail(SheetDto param) throws FileNotFoundException {
        String fileName = param.getExamCd() + "_" + param.getHallCd() + "_" + param.getScorerNm() + ".pdf";
        File file = new File(path, fileName);
        return ResponseEntity.ok(new InputStreamResource(new FileInputStream(file)));
    }

    @RequestMapping(value = "fixList.{format:colmodel|json}")
    public ResponseEntity fixList(@PathVariable String format, ScoreDto param, Pageable pageable){
        switch (format) {
            case COLMODEL:
                return ResponseEntity.ok(adminScoreService.getFixListModel());
            case JSON:
                return ResponseEntity.ok(adminScoreMapper.fixList(param, pageable));
            default:
                return null;
        }
    }
}
