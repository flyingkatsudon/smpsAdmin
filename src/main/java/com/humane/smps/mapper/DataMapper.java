package com.humane.smps.mapper;

import com.humane.smps.dto.*;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

@Mapper
public interface DataMapper {

    /*
        examinee: 수험생별 종합
        scorer: 채점자별 상세(세로)
        knuScorer: 경북대용 세로
        absentList: 결시자 리스트
        physical: 에리카 체육
        runningResult: 에리카 체육 달리기 결과값

        getScorerCnt(): 평가위원 수
        getItemCnt(): 평가항목 수
        ericaItemCnt(): 에리카 항목 수

        examMap, scorerH: 채점자별 상세(가로)를 만들기 위한 두 쿼리
        scoredH: 채점자별 상세(가로) 쿼리 1개용 - 느려서 사용 안 함
        skkuPeriod1: 성균관대 1단계용 산출물

        paperToSmps: 미술 답안지를 examMap.eval_cd로 가져갈 답안지 번호 추출
        fillEvalCd: 위에서 가져온 답안지 번호를 채워넣음
        fillList: 점수를 채워넣을 리스트 가져옴
        fillScore: 입력받은 점수를 일괄 입력함
        fillVirtNo: 가번호 일괄 입력
        scorerList: 평가위원 리스트

        examDetail: 시험 정보 편집에 나타낼 시험 정보
        drawData: 동점자 검사
        scoreUpload: 글로벌인재, 소프트웨어인재 업로드 양식
        sqlEdit: sql편집기 실행
        failList: F처리자 리스트 (서류평가)

        lawScoreUpload, medScoreUpload: 각각 법학, 의대 서류평가
        attendance: 출결리스트
     */

    Page<ExamineeDto> examinee(@Param("param") ExamineeDto param, @Param("pageable") Pageable pageable);

    Page<ExamineeDto> virtNoDoc(@Param("param") ExamineeDto param, @Param("pageable") Pageable pageable);

    Page<ScoreDto> scorer(@Param("param") ScoreDto param, @Param("pageable") Pageable pageable);

    Page<ScoreDto> knuScorer(@Param("param") ScoreDto param, @Param("pageable") Pageable pageable);

    Page<ExamineeDto> absentList(@Param("param") ExamineeDto param, @Param("pageable") Pageable pageable);

    Page<physicalDto> physical(@Param("param") physicalDto param, @Param("pageable") Pageable pageable);

    List<Map<String, String>> runningResult();

    long getScorerCnt();

    long getItemCnt();

    long ericaItemCnt(@Param("param") String param);

    Page<Map<String, Object>> examMap(@Param("param") ScoreDto param, @Param("pageable") Pageable pageable);

    List<Map<String, Object>> scorerH(@Param("param") Map map);

    // 채점자별 상세(가로) - 쿼리 1개
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