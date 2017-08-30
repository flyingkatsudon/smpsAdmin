package com.humane.smps.service;

/**
 * Created by Jeremy on 2017. 8. 30..
 */

import com.humane.smps.model.*;
import com.humane.smps.repository.*;
import com.humane.util.retrofit.QueryBuilder;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rx.Observable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StudentService {
    private final ExamMapRepository examMapRepository;

    @PersistenceContext
    private EntityManager entityManager;

    // 고려대 면접고사용
    public void saveOrder(ApiService apiService, String admissionCd) {

        Observable.range(0, Integer.MAX_VALUE)
                .concatMap(page -> apiService.examMap(new QueryBuilder().add("exam.admission.admissionCd", admissionCd).getMap(), page, Integer.MAX_VALUE))
                .takeUntil(page -> page.last)
                .flatMap(page -> {

                    for (ExamMap examMap : page.content) {

                        Exam exam = examMap.getExam();
                        Examinee examinee = examMap.getExaminee();

                        // 일반면접 검사
                        ExamMap find = examMapRepository.findOne(new BooleanBuilder()
                                .and(QExamMap.examMap.examinee.examineeCd.eq(examinee.getExamineeCd()))
                                .and(QExamMap.examMap.exam.examCd.eq(exam.getExamCd()))
                        );

                        examMap.set_id(null); // 서버에서 받아오는 ID가 같으면 덮어씌워버릴 수 있음. ID값 삭제

                        if (find != null) setExamMapOrder(examMap, find);
                    }

                    return Observable.from(page.content);
                })
                .toBlocking().first();
    }

    public void setExamMapOrder(ExamMap examMap, ExamMap find) {

        find.setHall(examMap.getHall());
        find.setGroupNm(examMap.getGroupNm());
        find.setGroupOrder(examMap.getGroupOrder());
        find.setDebateNm(examMap.getDebateNm());
        find.setDebateOrder(examMap.getDebateOrder());

        examMapRepository.save(find);
    }


    int cnt = 0;

    public boolean orderCnt(ApiService apiService, String admissionCd) {
        cnt = 0;

        Observable.range(0, Integer.MAX_VALUE)
                .concatMap(page -> apiService.examMap(new QueryBuilder().add("exam.admission.admissionCd", admissionCd).getMap(), page, Integer.MAX_VALUE))
                .takeUntil(page -> page.last)
                .flatMap(page -> {
                    for (ExamMap examMap : page.content) {
                        if (examMap.getGroupNm() != null) {
                            cnt += 1;
                            log.debug("{}", examMap);
                        }
                    }
                    return Observable.from(page.content);
                })
                .toBlocking().first();

        log.debug("{}", cnt);
        if (cnt > 0) return true;
        else return false;
    }
}
