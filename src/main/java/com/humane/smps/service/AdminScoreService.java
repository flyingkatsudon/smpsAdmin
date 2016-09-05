package com.humane.smps.service;

import com.humane.smps.mapper.AdminScoreMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdminScoreService {
    
    private final AdminScoreMapper mapper;

    public Object getFixListModel() {
        // 기본 생성
        List<ColModel> colModels = new ArrayList<>();

        long itemCnt = mapper.getItemCnt();
        colModels.add(new ColModel("virtNo", "가번호"));
        for (int i = 1; i <= itemCnt; i++) {
            colModels.add(new ColModel("score" + (i < 10 ? "0" + i : i), "항목" + i));
        }
        colModels.add(new ColModel("scorerNm", "평가위원"));
        colModels.add(new ColModel("rescoreDttm", "수정시간"));

        return colModels;
    }

    @Data
    private class ColModel {
        private String name;
        private String label;
        private boolean sortable = true;

        public ColModel(String name, String label) {
            this.name = name;
            this.label = label;
        }

        public ColModel(String name, String label, boolean sortable) {
            this(name, label);
            this.sortable = sortable;
        }
    }
}
