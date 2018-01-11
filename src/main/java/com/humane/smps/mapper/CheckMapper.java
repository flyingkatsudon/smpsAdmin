package com.humane.smps.mapper;

import com.humane.smps.dto.CheckDto;
import com.humane.smps.dto.ScoreDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

@Mapper
public interface CheckMapper {

    Page<CheckDto> scorer(@Param("param") CheckDto param, @Param("pageable") Pageable pageable);

    Page<CheckDto> item(@Param("param") CheckDto param, @Param("pageable") Pageable pageable);

    long getScorerCnt();

    long getItemCnt();

    Page<Map<String, Object>> scoredCnt(@Param("param") ScoreDto param, @Param("pageable") Pageable pageable);

    Page<Map<String, Object>> scoredF(@Param("param") ScoreDto param, @Param("pageable") Pageable pageable);
}
