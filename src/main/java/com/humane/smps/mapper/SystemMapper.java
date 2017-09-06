package com.humane.smps.mapper;

import com.humane.smps.dto.AccountDto;
import com.humane.smps.dto.ExamInfoDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Mapper
public interface SystemMapper {
    Page<AccountDto> account(@Param("param") AccountDto param, @Param("pageable") Pageable pageable);
    Page<AccountDto> admission(@Param("pageable") Pageable pageable);
    Page<AccountDto> accountDetail(@Param("userId") String userId, @Param("pageable") Pageable pageable);
    void addAccount(@Param("userId") String userId, @Param("password") String password);
    void addRole(@Param("userId") String userId, @Param("roleName") String roleName);
    void deleteAccount(@Param("userId") String userId);
    void deleteRole(@Param("userId") String userId);

    void insertAdmission(@Param("userId") String userId, @Param("admissionCd") String admissionCd);
    void deleteAdmission(@Param("userId") String userId);
    void modifyUser(@Param("userId") String userId, @Param("password") String password);
    void modifyRole(@Param("userId") String userId, @Param("roleName") String roleName);

    Page<AccountDto> idCheck(@Param("pageable") Pageable pageable);

    Page<ExamInfoDto> getStep1(@Param("param") ExamInfoDto param, @Param("pageable") Pageable pageable);
    Page<ExamInfoDto> getStep2(@Param("examCd") String examCd, @Param("pageable") Pageable pageable);
    Page<ExamInfoDto> getStep3(@Param("examCd") String examCd, @Param("pageable") Pageable pageable);
    void modifyStep1(@Param("param") ExamInfoDto param);
    void modifyStep2(@Param("param") ExamInfoDto param);
    void modifyStep3(@Param("param") ExamInfoDto param);
}