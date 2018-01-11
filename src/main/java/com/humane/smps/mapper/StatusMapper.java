package com.humane.smps.mapper;

import com.humane.smps.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Mapper
public interface StatusMapper {

    /*
     * all: 응시 상태 요약(점수가 있어야 체크)
     * 그 외: '모집단위', '전공', '고사실', '조' 순 통계
     *
     */

    StatusDto all(@Param("param") StatusDto param);

    Page<StatusDto> dept(@Param("param") StatusDto param, @Param("pageable") Pageable pageable);

    Page<StatusDto> major(@Param("param") StatusDto param, @Param("pageable") Pageable pageable);

    Page<StatusDto> hall(@Param("param") StatusDto param, @Param("pageable") Pageable pageable);

    Page<StatusDto> group(@Param("param") StatusDto param, @Param("pageable") Pageable pageable);
}
