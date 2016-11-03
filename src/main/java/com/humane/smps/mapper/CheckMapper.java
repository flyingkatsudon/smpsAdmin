package com.humane.smps.mapper;

import com.humane.smps.dto.CheckItemDto;
import com.humane.smps.dto.CheckScorerDto;
import com.humane.smps.dto.CheckSendDto;
import com.humane.smps.dto.ScoreDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

@Mapper
public interface CheckMapper {

    Page<CheckSendDto> send(@Param("param") CheckSendDto param, @Param("pageable") Pageable pageable);

    Page<CheckScorerDto> scorer(@Param("param") CheckScorerDto param, @Param("pageable") Pageable pageable);

    Page<CheckItemDto> item(@Param("param") CheckItemDto param, @Param("pageable") Pageable pageable);

    long getScorerCnt();

    long getItemCnt();

    Page<Map<String, Object>> scoredCnt(@Param("param") ScoreDto param, @Param("pageable") Pageable pageable);

    Page<Map<String, Object>> scoredF(@Param("param") ScoreDto param, @Param("pageable") Pageable pageable);
}
