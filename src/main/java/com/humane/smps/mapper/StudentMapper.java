package com.humane.smps.mapper;

import com.humane.smps.dto.ExamineeDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by Jeremy on 2017. 8. 30..
 */
@Mapper
public interface StudentMapper {
    long orderCnt(@Param("param") String admissionCd);
    Page<ExamineeDto> order(@Param("param") ExamineeDto param, @Param("pageable") Pageable pageable);
}
