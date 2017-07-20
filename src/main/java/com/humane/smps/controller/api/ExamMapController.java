package com.humane.smps.controller.api;

import com.humane.smps.model.ExamMap;
import com.humane.smps.model.QExamMap;
import com.humane.smps.repository.ExamMapRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "api/examMap", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExamMapController {
    private final ExamMapRepository repository;

    @RequestMapping(method = RequestMethod.GET)
    public Page<ExamMap> index(@QuerydslPredicate Predicate predicate, @PageableDefault Pageable pageable) {
        return repository.findAll(predicate, pageable);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ExamMap> merge(@RequestHeader(name="updateHall") Boolean updateHall, @RequestBody ExamMap examMap) {
        // 기존에 있을 경우 update , 없을 경우 insert

        // 찾기 examinee, exam, hall
        QExamMap qExamMap = QExamMap.examMap;
        log.debug("{}", examMap);

        if(!updateHall){

            ExamMap findMap = repository.findOne(new BooleanBuilder()
                    .and(qExamMap.examinee.eq(examMap.getExaminee()))
                    .and(qExamMap.exam.eq(examMap.getExam()))
                    .and(qExamMap.hall.eq(examMap.getHall()))
            );

            // pk 지정. pk가 있을 경우는 update된다.
            if (findMap != null) examMap.set_id(findMap.get_id());

            ExamMap rtn = repository.save(examMap);

            // 하위 시험이 존재할 경우 하위 시험의 가번호를 강제로 설정한다.
            String fkExamCd = examMap.getExam().getExamCd();
            String virtNo = examMap.getVirtNo();

            repository.findAll(new BooleanBuilder()
                    .and(qExamMap.examinee.eq(examMap.getExaminee()))
                    .and(qExamMap.exam.fkExam.examCd.eq(fkExamCd))
                    .and(qExamMap.hall.eq(examMap.getHall()))
            ).forEach(examMap1 -> {
                examMap1.setVirtNo(virtNo);
                repository.save(examMap1);
            });

            return new ResponseEntity<>(rtn, HttpStatus.OK);

        }
        else{
            ExamMap findMap = repository.findOne(new BooleanBuilder()
                    .and(qExamMap.examinee.eq(examMap.getExaminee()))
                    .and(qExamMap.exam.eq(examMap.getExam()))
            );

            // pk 지정. pk가 있을 경우는 update된다.
            if (findMap != null) examMap.set_id(findMap.get_id());

            ExamMap rtn = repository.save(examMap);

            // 하위 시험이 존재할 경우 하위 시험의 가번호를 강제로 설정한다.
            String fkExamCd = examMap.getExam().getExamCd();
            String virtNo = examMap.getVirtNo();

            repository.findAll(new BooleanBuilder()
                    .and(qExamMap.examinee.eq(examMap.getExaminee()))
                    .and(qExamMap.exam.fkExam.examCd.eq(fkExamCd))
            ).forEach(examMap1 -> {
                examMap1.setVirtNo(virtNo);
                repository.save(examMap1);
            });

            return new ResponseEntity<>(rtn, HttpStatus.OK);
        }

    }

    @RequestMapping(value = "list", method = RequestMethod.POST)
    public ResponseEntity<Iterable<ExamMap>> merge(@RequestBody Iterable<ExamMap> examMaps) {
        Iterable<ExamMap> rtn = repository.save(examMaps);
        return new ResponseEntity<>(rtn, HttpStatus.OK);
    }
}