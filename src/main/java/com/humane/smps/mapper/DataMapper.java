package com.humane.smps.mapper;

import com.humane.smps.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

@Mapper
public interface DataMapper {

    Page<ExamineeDto> examinee(@Param("param") ExamineeDto param, @Param("pageable") Pageable pageable);

    Page<ScoreDto> scorer(@Param("param") ScoreDto param, @Param("pageable") Pageable pageable);

    Page<ScoreDto> knuScorer(@Param("param") ScoreDto param, @Param("pageable") Pageable pageable);

    Page<ExamineeDto> absentList(@Param("param") ExamineeDto param, @Param("pageable") Pageable pageable);

    Page<physicalDto> physical(@Param("param") physicalDto param, @Param("pageable") Pageable pageable);

    List<Map<String, String>> runningResult();

    long getScorerCnt();

    long getItemCnt();

    long ericaItemCnt(@Param("param") String param);

    List<Map<String, Object>> scorerH(@Param("param") Map map);

    // 채점자별 상세(가로)
    Page<Map<String, Object>> scoredH(@Param("param") ScoreDto param, @Param("pageable") Pageable pageable);

    // 성균관대 연기 1단게
    Page<Map<String, Object>> skkuPeriod1(@Param("param") ScoreDto param, @Param("pageable") Pageable pageable);

    List<EvalDto> paperToSmps(@Param("param") EvalDto param);

    void fillEvalCd(@Param("param") EvalDto param);

    List<EvalDto> fillList(@Param("param") EvalDto param);

    void fillScore(@Param("param") EvalDto param);

    List<ScoreDto> scorerList(@Param("param") ScoreDto param);

    List<ExamDto> ericaExamInfo(@Param("param") String param);

    List<ExamDto> examDetail(@Param("examCd") String examCd);

    void fillVirtNo(@Param("param") ExamDto param);

    Page<Map<String, Object>> drawData(@Param("param") ScoreDto param, @Param("pageable") Pageable pageable);

    Page<ScoreUploadDto> scoreUpload(@Param("param") ScoreUploadDto param, @Param("pageable") Pageable pageable);

    List<Map<String, String>> sqlEdit(@Param("sql") String sql);

    Page<ExamineeDto> failList(@Param("param") ExamineeDto param, @Param("pageable") Pageable pageable);

    Page<ScoreUploadDto> lawScoreUpload(@Param("param") ScoreUploadDto param, @Param("pageable") Pageable pageable);

    Page<ScoreUploadDto> medScoreUpload(@Param("param") ScoreUploadDto param, @Param("pageable") Pageable pageable);

    Page<ExamineeDto> attendance(@Param("param") ExamineeDto param, @Param("pageable") Pageable pageable);
}