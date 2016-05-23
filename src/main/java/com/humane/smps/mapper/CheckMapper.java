package com.humane.smps.mapper;

import com.humane.smps.dto.CheckItemDto;
import com.humane.smps.dto.CheckScorerDto;
import com.humane.smps.dto.SendDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Mapper
public interface CheckMapper {

    Page<SendDto> send(@Param("param") SendDto param, @Param("pageable") Pageable pageable);

    Page<CheckScorerDto> scorer(@Param("param") CheckScorerDto param, @Param("pageable") Pageable pageable);

    Page<CheckItemDto> item(@Param("param") CheckItemDto param, @Param("pageable") Pageable pageable);
}
