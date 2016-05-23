package com.humane.smps.mapper;

import com.humane.smps.dto.ExamineeDto;
import com.humane.smps.dto.ScoreDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Mapper
public interface DataMapper {

    Page<ExamineeDto> examinee(@Param("param") ExamineeDto param, @Param("pageable") Pageable pageable);

    Page<ScoreDto> score(@Param("param") ScoreDto param, @Param("pageable") Pageable pageable);
}
