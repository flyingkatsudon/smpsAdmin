package com.humane.smps.service;

import com.humane.smps.dto.UploadItemDto;
import com.humane.smps.model.Devi;
import com.humane.smps.model.Exam;
import com.humane.smps.model.Item;
import com.humane.smps.model.QItem;
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
public class SettingService {
    private final ItemRepository itemRepository;

    public long saveItems(UploadItemDto dto) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        QItem qItem = QItem.item;
        // exam
        Exam exam = new Exam();
        exam.setExamCd(dto.getExamCd());
        long itemCnt = Long.parseLong(dto.getItemCnt());

        for (long i = 1; i <= itemCnt; i++) {
            String itemNo = (String) new PropertyDescriptor("itemNo" + i, UploadItemDto.class).getReadMethod().invoke(dto);
            String itemNm = (String) new PropertyDescriptor("itemNm" + i, UploadItemDto.class).getReadMethod().invoke(dto);
            String deviCd = (String) new PropertyDescriptor("deviCd" + i, UploadItemDto.class).getReadMethod().invoke(dto);

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
