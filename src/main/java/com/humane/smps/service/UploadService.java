package com.humane.smps.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.humane.smps.form.FormItemVo;
import com.humane.smps.model.*;
import com.humane.smps.repository.ExamRepository;
import com.humane.smps.repository.ItemRepository;
import com.mysema.query.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

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
                .and(QExam.exam.examNm.eq(exam.getExamNm()))
                .and(QExam.exam.examDate.eq(exam.getExamDate()))
                .and(QExam.exam.examTime.eq(exam.getExamTime()))
        );
        long itemCnt = Long.parseLong(dto.getItemCnt());

        for (long i = 1; i <= itemCnt; i++) {
            String itemNo = (String) new PropertyDescriptor("itemNo" + i, FormItemVo.class).getReadMethod().invoke(dto);
            String itemNm = (String) new PropertyDescriptor("itemNm" + i, FormItemVo.class).getReadMethod().invoke(dto);
            String deviCd = (String) new PropertyDescriptor("deviCd" + i, FormItemVo.class).getReadMethod().invoke(dto);

            Devi devi = new Devi();
            devi.setDeviCd(deviCd);

            BooleanBuilder predicate = new BooleanBuilder()
                    .and(qItem.exam.examCd.eq(exam.getExamCd()))
                    .and(qItem.itemNo.eq(itemNo));

            Item item = itemRepository.findOne(predicate);

            if (item == null) { // insert
                item = new Item();
                item.setItemNo(itemNo);
                item.setItemNm(itemNm);
                item.setDevi(devi);
                item.setExam(exam);
                item.setOrderby(i);
            } else { // update
                item.setItemNm(itemNm);
                item.setDevi(devi);
                item.setOrderby(i);
            }
            itemRepository.save(item);
        }
        return itemRepository.count(new BooleanBuilder().and(qItem.exam.examCd.eq(dto.getExamCd())));
    }
}
