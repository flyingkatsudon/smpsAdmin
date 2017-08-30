package com.humane.smps.controller.admin;

import com.humane.smps.dto.ExamineeDto;
import com.humane.smps.mapper.StudentMapper;
import com.humane.smps.service.ApiService;
import com.humane.smps.service.StudentService;
import com.humane.util.jasperreports.JasperReportsExportHelper;
import com.humane.util.retrofit.ServiceBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "student")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StudentController {

    private final StudentService studentService;
    private final StudentMapper studentMapper;

    private static final String JSON = "json";

    /**
     * 고려대 면접고사용
     */
    @RequestMapping(value = "local/orderCnt")
    public boolean fromLocal(String admissionCd){

        try {
            long check = studentMapper.orderCnt(admissionCd);

            if(check <= 0)
                return false;

        }catch(Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @RequestMapping(value = "server/orderCnt")
    public boolean fromServer(String admissionCd, String url) {

        try {
            // 2. create http service
            ApiService apiService = ServiceBuilder.INSTANCE.createService(url, ApiService.class);

            // 3. getData(iterator)
            return studentService.orderCnt(apiService, admissionCd);
        }catch(Exception e){
            return false;
        }
    }

    @RequestMapping(value = "saveOrder")
    public ResponseEntity saveOrder(String admissionCd, String url) {

        try {
            // 1. validate wrapper
            if (StringUtils.isEmpty(url))
                return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("url이 올바르지 않습니다.");

            // 2. create http service
            ApiService apiService = ServiceBuilder.INSTANCE.createService(url, ApiService.class);

            // 3. getData(iterator)
            studentService.saveOrder(apiService, admissionCd);

            return ResponseEntity.ok("데이터가 정상적으로 처리되었습니다.");
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("관리자에게 문의하세요");
        }
    }

    @RequestMapping(value = "order.{format:json|pdf|xls|xlsx}")
    public ResponseEntity order(@PathVariable String format, ExamineeDto param, Pageable pageable) {
        switch (format) {
            case JSON:
                return ResponseEntity.ok(studentMapper.order(param, pageable));
            default:
                // TODO: 순번 jrxml 수정해야
                return JasperReportsExportHelper.toResponseEntity(
                        "jrxml/system-order.jrxml"
                        , format
                        , studentMapper.order(param, new PageRequest(0, Integer.MAX_VALUE, pageable.getSort())).getContent());
        }
    }
}