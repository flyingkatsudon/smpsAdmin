package com.humane.smps.service;

import com.humane.smps.dto.ScoreDto;
import com.humane.smps.mapper.DataMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DataService {
    private final DataMapper mapper;

    public Object getExamineeModel() {
        // 기본 생성
        List<ColModel> colModels = new ArrayList<>();
        colModels.add(new ColModel("admissionNm", "전형"));
        colModels.add(new ColModel("examDate", "시험일자"));
        colModels.add(new ColModel("examTime", "시험시간"));
        colModels.add(new ColModel("deptNm", "모집단위"));
        colModels.add(new ColModel("majorNm", "전공"));
        colModels.add(new ColModel("headNm", "고사본부"));
        colModels.add(new ColModel("bldgNm", "고사건물"));
        colModels.add(new ColModel("hallNm", "고사실"));
        colModels.add(new ColModel("virtNo", "가번호"));

        for (int i = 1; i <= mapper.getItemCnt(); i++)
            colModels.add(new ColModel("avgScore" + i, "항목" + i + "평균"));

        colModels.add(new ColModel("scorerCnt", "평가위원수"));
        colModels.add(new ColModel("isAttend", "응시여부"));
        return colModels;
    }

    public List<ColModel> getScorerModel() {
        // 기본 생성
        List<ColModel> colModels = new ArrayList<>();
        colModels.add(new ColModel("admissionNm", "전형"));
        colModels.add(new ColModel("examDate", "시험일자"));
        colModels.add(new ColModel("examTime", "시험시간"));
        colModels.add(new ColModel("deptNm", "모집단위"));
        colModels.add(new ColModel("majorNm", "전공"));
        colModels.add(new ColModel("virtNo", "가번호"));
        colModels.add(new ColModel("scorerNm", "평가위원"));

        for (int i = 1; i <= mapper.getItemCnt(); i++)
            colModels.add(new ColModel("score0" + i, "항목" + i));

        colModels.add(new ColModel("totalScore", "총점"));
        colModels.add(new ColModel("memo", "메모"));
        colModels.add(new ColModel("isPhoto", "사진"));
        colModels.add(new ColModel("scoreDttm", "채점시간"));

        return colModels;
    }

    @Data
    private class ColModel {
        private String name;
        private String label;
        private boolean sortable = true;

        public ColModel(String name, String label){
            this.name = name;
            this.label = label;
        }

        public ColModel(String name, String label, boolean sortable){
            this(name, label);
            this.sortable = sortable;
        }
    }

    public Page<Map<String, Object>> getScorerH(ScoreDto param, Pageable pageable) {
        Page<Map<String, Object>> page = mapper.examMap(param, pageable);
        page.forEach(map -> {
            String examCd = map.get("examCd") == null ? null : map.get("examCd").toString();
            String virtNo = map.get("virtNo") == null ? null : map.get("virtNo").toString();

            if(examCd != null && virtNo != null){
                List<Map<String, Object>> scoreList = mapper.scorerH(map);
                for(int i = 1; i <= scoreList.size(); i++){
                    Map<String, Object> score = scoreList.get(i-1);

                    if(score != null){
                        map.put("scorerNm"+ i, score.get("scorerNm"));
                        map.put("score"+ i + "S1", score.get("score01"));
                        map.put("score"+ i + "S2", score.get("score02"));
                        map.put("score"+ i + "S3", score.get("score03"));
                        map.put("score"+ i + "S4", score.get("score04"));
                        map.put("score"+ i + "S5", score.get("score05"));
                        map.put("score"+ i + "S6", score.get("score06"));
                        map.put("score"+ i + "S7", score.get("score07"));
                        map.put("score"+ i + "S8", score.get("score08"));
                        map.put("score"+ i + "S9", score.get("score09"));
                        map.put("score"+ i + "S10", score.get("score10"));
                        map.put("scoreDttm"+ i, score.get("scoreDttm"));
                    }
                }
            }
        });

        return page;
    }

    public List<ColModel> getScorerHModel() {
        // 기본 생성
        List<ColModel> colModels = new ArrayList<>();
        colModels.add(new ColModel("admissionNm", "전형"));
        colModels.add(new ColModel("examDate", "시험일자"));
        colModels.add(new ColModel("examTime", "시험시간"));
        colModels.add(new ColModel("deptNm", "모집단위"));
        colModels.add(new ColModel("majorNm", "전공"));
        colModels.add(new ColModel("virtNo", "가번호"));

        long scorerCnt = mapper.getScorerCnt(); // 채점자수
        long itemCnt = mapper.getItemCnt(); // 항목수

        for (int i = 1; i <= scorerCnt; i++) {
            colModels.add(new ColModel("scorerNm" + i, "평가위원", false));
            for (int j = 1; j <= itemCnt; j++) colModels.add(new ColModel("score" + i + "S" + j, "항목" + j, false));

            colModels.add(new ColModel("scoreDttm" + i, "채점시간", false));
        }
        return colModels;
    }
}
