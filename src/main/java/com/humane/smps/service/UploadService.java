package com.humane.smps.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.humane.smps.form.FormItemVo;
import com.humane.smps.model.*;
import com.humane.smps.repository.ExamRepository;
import com.humane.smps.repository.ItemRepository;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UploadService {
    private final ItemRepository itemRepository;
    private final ExamRepository examRepository;

    public long saveItems(FormItemVo dto) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        QItem qItem = QItem.item;
        // exam
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Exam exam = mapper.convertValue(dto, Exam.class);
        exam = examRepository.findOne(new BooleanBuilder()
                .and(QExam.exam.examCd.eq(exam.getExamCd()))
                .and(QExam.exam.examNm.eq(exam.getExamNm()))
                .and(QExam.exam.examDate.eq(exam.getExamDate()))
        );
        long itemCnt = Long.parseLong(dto.getItemCnt());

        for (long i = 1; i <= itemCnt; i++) {
            String itemNo = (String) new PropertyDescriptor("itemNo" + i, FormItemVo.class).getReadMethod().invoke(dto);
            String itemNm = (String) new PropertyDescriptor("itemNm" + i, FormItemVo.class).getReadMethod().invoke(dto);

            String maxScore = (String) new PropertyDescriptor("maxScore" + i, FormItemVo.class).getReadMethod().invoke(dto);
            String minScore = (String) new PropertyDescriptor("minScore" + i, FormItemVo.class).getReadMethod().invoke(dto);

            String keypadType = (String) new PropertyDescriptor("keypadType" + i, FormItemVo.class).getReadMethod().invoke(dto);
            String scoreMap = (String) new PropertyDescriptor("scoreMap" + i, FormItemVo.class).getReadMethod().invoke(dto);

            Item item = itemRepository.findOne(new BooleanBuilder()
                    .and(qItem.exam.examCd.eq(exam.getExamCd()))
                    .and(qItem.itemNo.eq(itemNo))
            );

            if (item == null) { // insert
                item = new Item();
                item.setItemNo(itemNo);
                item.setItemNm(itemNm);
                item.setExam(exam);
                item.setOrderby(i);

                // min, max 값이 반드시 존재하면 Long으로 변환 후 저장
                item.setMaxScore(validate(maxScore));
                item.setMinScore(validate(minScore));

                // keypadType이 null이면 기본값으로 0
                if (keypadType == null) item.setKeypadType("0");

                // keypadType이 0이면 기본값으로 min: 0, max: 100을 입력
                item.setKeypadType(keypadType);
                if (keypadType.equals("0")) {
                    item.setMaxScore(validate("100"));
                    item.setMinScore(validate("0"));
                    item.setMaxWarning(validate("10"));
                    item.setMinWarning(validate("0"));
                }

                item.setScoreMap(scoreMap);
                if (scoreMap.equals("")) item.setScoreMap(null);

            } else { // update
                item.setItemNm(itemNm);
                item.setOrderby(i);

                item.setMaxScore(validate(maxScore));
                item.setMinScore(validate(minScore));
                item.setKeypadType(keypadType);

                item.setScoreMap(scoreMap);
                if (scoreMap.equals("")) item.setScoreMap(null);
            }
            itemRepository.save(item);
        }
        return itemRepository.count(new BooleanBuilder().and(qItem.exam.examCd.eq(dto.getExamCd())));
    }

    public Long validate(String val) {
        if (val != null) {
            if (!val.equals("")) return Long.parseLong(val);
        }
        return null;
    }
}
