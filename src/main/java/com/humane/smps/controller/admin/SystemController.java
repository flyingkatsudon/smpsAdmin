package com.humane.smps.controller.admin;

import com.humane.smps.dto.*;
import com.humane.smps.mapper.SystemMapper;
import com.humane.smps.model.*;
import com.humane.smps.repository.UserAdmissionRepository;
import com.humane.smps.repository.UserRepository;
import com.humane.smps.repository.UserRoleRepository;
import com.humane.smps.service.ApiService;
import com.humane.smps.service.SystemService;
import com.humane.util.jasperreports.JasperReportsExportHelper;
import com.humane.util.retrofit.ServiceBuilder;
import com.humane.util.spring.Page;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;

@RestController
@RequestMapping(value = "system")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SystemController {
    private final UserRepository userRepository;
    private final UserAdmissionRepository userAdmissionRepository;
    private final UserRoleRepository userRoleRepository;

    private final SystemService systemService;
    private final SystemMapper systemMapper;

    private static final String JSON = "json";

    @RequestMapping(value = "server.json", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity serverList() {
        ApiService apiService = ServiceBuilder.INSTANCE.createService("http://update.humanesystem.com:10000", ApiService.class);
        try {
            return ResponseEntity.ok(apiService.checkUrl().toBlocking().first());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "examList.json", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity examList(
            @RequestParam(required = false, defaultValue = "") String url,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "") String sort) {

        if (StringUtils.isEmpty(url) || url.trim().length() == 0) { // 선택된 서버가 없다면 empty
            return ResponseEntity.ok(new Page());
        }

        ApiService apiService = ServiceBuilder.INSTANCE.createService(url, ApiService.class);
        try {
            return ResponseEntity.ok(apiService.exam(new HashMap<>(), page, size, sort).toBlocking().first());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "download", method = RequestMethod.POST)
    public ResponseEntity download(@RequestBody DownloadWrapper downloadWrapper) {
        try {
            // 1. validate wrapper
            if (StringUtils.isEmpty(downloadWrapper.getUrl()))
                return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("url이 올바르지 않습니다.");

            for (DownloadWrapper.Wrapper wrapper : downloadWrapper.getList()) {
                if (StringUtils.isEmpty(wrapper.getExamCd()))
                    return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("시험코드 및 고사실이 올바르지 않습니다.");
            }

            // 2. create http service
            ApiService apiService = ServiceBuilder.INSTANCE.createService(downloadWrapper.getUrl(), ApiService.class);

            // 3. getData(iterator)
            systemService.saveExamHallDate(apiService, downloadWrapper);
            systemService.saveExamMap(apiService, downloadWrapper);
            systemService.saveItem(apiService, downloadWrapper);

            return ResponseEntity.ok("데이터가 정상적으로 처리되었습니다.");
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("관리자에게 문의하세요");
        }
    }

    @RequestMapping(value = "reset")
    public ResponseEntity reset(@RequestParam(defaultValue = "false") boolean photo) throws IOException {
        try{
            systemService.resetData(photo);
            return ResponseEntity.ok("삭제가 완료되었습니다.&nbsp;&nbsp;클릭하여 창을 종료하세요.");
        }catch(Exception e){
            log.debug("{}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("관리자에게 문의하세요.");
        }
    }

    @RequestMapping(value = "init")
    public ResponseEntity init(String examCd) throws IOException {
        try{
            systemService.initData(examCd);
            return ResponseEntity.ok("초기화가 완료되었습니다.&nbsp;&nbsp;클릭하여 창을 종료하세요.");
        }catch(Exception e){
            log.debug("{}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("관리자에게 문의하세요.");
        }
    }

    @RequestMapping(value = "account")
    public ResponseEntity account(AccountDto accountDto, Pageable pageable) {
        return ResponseEntity.ok(systemMapper.account(accountDto, pageable));
    }

    @RequestMapping(value = "admission")
    public ResponseEntity admission(Pageable pageable) {
        return ResponseEntity.ok(systemMapper.admission(pageable).getContent());
    }

    @RequestMapping(value = "accountDetail")
    public ResponseEntity accountDetail(String userId, Pageable pageable) {
        return ResponseEntity.ok(systemMapper.accountDetail(userId, pageable).getContent());
    }

    @RequestMapping(value = "delAdm")
    public void deleteAdmission(String userId) {
        systemMapper.deleteAdmission(userId);
    }

    @RequestMapping(value = "mod")
    public void modify(String userId, String roleName, String admissionCd, String password) {

        if (admissionCd != null) {
            // 기존 여부 확인
            UserAdmission find = userAdmissionRepository.findOne(new BooleanBuilder()
                    .and(QUserAdmission.userAdmission.user.userId.eq(userId))
                    .and(QUserAdmission.userAdmission.admission.admissionCd.eq(admissionCd)));

            if (find == null) systemMapper.insertAdmission(userId, admissionCd);
        }else {
            // 계정 정보 수정
            if (roleName != null) systemMapper.modifyRole(userId, roleName);
        }

        systemMapper.modifyUser(userId, password);
    }

    @RequestMapping(value = "addAccount")
    public void addAccount(String userId, String password, String roleName) {
        try {
            User findUser = userRepository.findOne(new BooleanBuilder()
                    .and(QUser.user.userId.eq(userId)));

            if (findUser == null) {
                systemMapper.addAccount(userId, password);
            }
        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            systemMapper.addRole(userId, roleName);
        }
    }

    @RequestMapping(value = "delAccount")
    public void deleteAccount(String userId) {
        systemMapper.deleteAdmission(userId);
        systemMapper.deleteRole(userId);
        systemMapper.deleteAccount(userId);
    }

    @RequestMapping(value = "idCheck")
    public ResponseEntity idCheck(Pageable pageable) {
        return ResponseEntity.ok(systemMapper.idCheck(pageable).getContent());
    }

    @RequestMapping(value = "getExamInfo")
    public ResponseEntity getExamInfo(ExamInfoDto param, Pageable pageable) {
        return ResponseEntity.ok(systemMapper.getExamInfo(param, pageable).getContent());
    }

    @RequestMapping(value = "modifyExamInfo")
    @Transactional(propagation= Propagation.REQUIRED, rollbackFor={Throwable.class})
    public void modifyExamInfo(@RequestBody ExamInfoDto param) {
        systemMapper.modifyExamInfo(param);
        systemMapper.modifyExamHallDateOfExamInfo(param);
    }

    /**
     * 고려대 면접고사용
     */
    @RequestMapping(value = "check/order")
    public boolean check(String admissionCd){

        try {
            long check = systemMapper.check(admissionCd);

            if(check <= 0)
                return false;

        }catch(Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
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
            systemService.saveOrder(apiService, admissionCd);

            return ResponseEntity.ok("데이터가 정상적으로 처리되었습니다.");
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("관리자에게 문의하세요");
        }
    }

    @RequestMapping(value = "order.{format:json|pdf|xls|xlsx}")
    public ResponseEntity order(@PathVariable String format, ExamineeDto param, Pageable pageable) {
        switch (format) {
            case JSON:
                return ResponseEntity.ok(systemMapper.order(param, pageable));
            default:
                // TODO: 순번 jrxml 수정해야
                return JasperReportsExportHelper.toResponseEntity(
                        "jrxml/status-dept.jrxml"
                        , format
                        , systemMapper.order(param, new PageRequest(0, Integer.MAX_VALUE, pageable.getSort())).getContent());
        }
    }
}