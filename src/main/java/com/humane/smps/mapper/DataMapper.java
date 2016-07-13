package com.humane.smps.mapper;

import com.humane.smps.dto.ExamineeDto;
import com.humane.smps.dto.ScoreDto;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.File;
import java.util.List;
import java.util.Map;

@Mapper
public interface DataMapper {

    Page<ExamineeDto> examinee(@Param("param") ExamineeDto param, @Param("pageable") Pageable pageable);

    Page<ScoreDto> scorer(@Param("param") ScoreDto param, @Param("pageable") Pageable pageable);

    long getScorerCnt();

    long getItemCnt();

    Page<Map<String, Object>> examMap(@Param("param") ScoreDto param, @Param("pageable")Pageable pageable);

    List<Map<String, Object>> scorerH(@Param("param") Map map);

}
