package com.humane.smps.mapper;

import com.humane.smps.dto.ScoreDto;
import com.humane.smps.dto.ScoreFixDto;
import com.humane.smps.dto.SheetDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Mapper
public interface AdminScoreMapper {

    Page<SheetDto> sheet(@Param("param") SheetDto param, @Param("pageable") Pageable pageable);

    Page<ScoreFixDto> fix(@Param("param") ScoreFixDto param, @Param("pageable") Pageable pageable);

    Page<ScoreDto> fixList(@Param("param") ScoreDto param, @Param("pageable") Pageable pageable);

    long getItemCnt();
}
