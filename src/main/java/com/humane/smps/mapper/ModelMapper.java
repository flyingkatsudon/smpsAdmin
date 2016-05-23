package com.humane.smps.mapper;

import com.humane.smps.dto.StatusDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ModelMapper {

    List<StatusDto> toolbar(@Param("param") StatusDto param);
}
