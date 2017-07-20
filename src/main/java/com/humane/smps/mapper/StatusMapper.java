package com.humane.smps.mapper;

import com.humane.smps.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Mapper
public interface StatusMapper {
    StatusDto all(@Param("param") StatusDto param);

    Page<StatusDeptDto> dept(@Param("param") StatusDeptDto param, @Param("pageable") Pageable pageable);

    Page<StatusMajorDto> major(@Param("param") StatusMajorDto param, @Param("pageable") Pageable pageable);

    Page<StatusHallDto> hall(@Param("param") StatusHallDto param, @Param("pageable") Pageable pageable);

    Page<StatusGroupDto> group(@Param("param") StatusGroupDto param, @Param("pageable") Pageable pageable);

    Page<ExamInfoDto> getExamInfo(@Param("param") ExamInfoDto param, @Param("pageable") Pageable pageable);

    void modifyExamInfo(@Param("param") ExamInfoDto param);
    void modifyExamHallDateOfExamInfo(@Param("param") ExamInfoDto param);
}
