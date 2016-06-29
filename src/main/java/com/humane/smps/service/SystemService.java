package com.humane.smps.service;

import com.humane.smps.dto.DownloadWrapper;
import com.humane.smps.model.*;
import com.humane.smps.repository.*;
import com.humane.util.retrofit.QueryBuilder;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.hibernate.HibernateDeleteClause;
import com.mysema.query.jpa.hibernate.HibernateQuery;
import com.mysema.query.jpa.hibernate.HibernateUpdateClause;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rx.Observable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SystemService {
    private final DeviRepository deviRepository;
    private final AdmissionRepository admissionRepository;
    private final ExamRepository examRepository;
    private final HallRepository hallRepository;
    private final ExamHallRepository examHallRepository;
    private final ExamineeRepository examineeRepository;
    private final ExamMapRepository examMapRepository;
    private final ItemRepository itemRepository;

    @PersistenceContext private EntityManager entityManager;

    @Transactional
    public void resetData() {
        new HibernateDeleteClause(entityManager.unwrap(Session.class), QSheet.sheet).execute();
        new HibernateDeleteClause(entityManager.unwrap(Session.class), QScore.score).execute();
        new HibernateDeleteClause(entityManager.unwrap(Session.class), QExamHall.examHall).execute();

        ScrollableResults scrollableResults = new HibernateQuery(entityManager.unwrap(Session.class))
                .from(QExamMap.examMap)
                .setFetchSize(Integer.MIN_VALUE)
                .scroll(ScrollMode.FORWARD_ONLY);

        while (scrollableResults.next()) {
            ExamMap examMap = (ExamMap) scrollableResults.get(0);
            try {
                new HibernateDeleteClause(entityManager.unwrap(Session.class), QExamMap.examMap)
                        .where(QExamMap.examMap.examinee.eq(examMap.getExaminee()))
                        .execute();
                new HibernateDeleteClause(entityManager.unwrap(Session.class), QExaminee.examinee)
                        .where(QExaminee.examinee.eq(examMap.getExaminee()))
                        .execute();
            } catch (Exception ignored) {
            }
        }
        scrollableResults.close();

        new HibernateDeleteClause(entityManager.unwrap(Session.class), QHall.hall).execute();
        new HibernateDeleteClause(entityManager.unwrap(Session.class), QItem.item).execute();

        // 편차의 경우 재귀구조이기 때문에 하위 데이터를 먼저 삭제 후 상위 데이터를 제거해야한다.
        new HibernateDeleteClause(entityManager.unwrap(Session.class), QDevi.devi1)
                .where(QDevi.devi1.devi.isNotNull())
                .execute();
        new HibernateDeleteClause(entityManager.unwrap(Session.class), QDevi.devi1).execute();

        new HibernateDeleteClause(entityManager.unwrap(Session.class), QExam.exam).execute();
    }

    public void initData() {
        long c = new HibernateDeleteClause(entityManager.unwrap(Session.class), QSheet.sheet).execute();
        long b = new HibernateDeleteClause(entityManager.unwrap(Session.class), QScore.score).execute();

        QExamMap examMap = QExamMap.examMap;
        long a = new HibernateUpdateClause(entityManager.unwrap(Session.class), examMap)
                .setNull(examMap.virtNo)
                .setNull(examMap.scanDttm)
                .setNull(examMap.photoNm)
                .setNull(examMap.memo)
                .setNull(examMap.evalCd)
                .execute();

        log.debug("{}, {}, {}", c, b, a);
    }

    public void saveDevi(ApiService apiService) {
        Observable.range(0, Integer.MAX_VALUE)
                .concatMap(page -> apiService.devi(new HashMap<>(), page, Integer.MAX_VALUE, null))
                .takeUntil(page -> page.last)
                .map(page -> deviRepository.save(page.content))
                .toBlocking().first();
    }

    public void saveExamHall(ApiService apiService, DownloadWrapper wrapper) {
        for (DownloadWrapper.ExamHallWrapper examHallWrapper : wrapper.getList()) {
            String newExamCd = examHallWrapper.getExamCd();
            String newHallCd = examHallWrapper.getHallCd();
            Observable.range(0, Integer.MAX_VALUE)
                    .concatMap(page -> apiService.examMap(new QueryBuilder().add("exam.examCd", newExamCd).add("hall.hallCd", newHallCd).getMap(), page, Integer.MAX_VALUE, null))
                    .takeUntil(page -> page.last)
                    .map(page -> {
                        for (ExamMap examMap : page.content) {
                            log.debug("{}", examMap);

                            admissionRepository.save(examMap.getExam().getAdmission());
                            examRepository.save(examMap.getExam());
                            hallRepository.save(examMap.getHall());

                            ExamHall examHall = examHallRepository.findOne(new BooleanBuilder()
                                    .and(QExamHall.examHall.exam.eq(examMap.getExam()))
                                    .and(QExamHall.examHall.hall.eq(examMap.getHall()))
                            );

                            if (examHall == null) {
                                examHall = new ExamHall();
                                examHall.setExam(examMap.getExam());
                                examHall.setHall(examMap.getHall());
                            }
                            examHallRepository.save(examHall);
                            examineeRepository.save(examMap.getExaminee());

                            ExamMap findExamMap = examMapRepository.findOne(new BooleanBuilder()
                                    .and(QExamMap.examMap.examinee.eq(examMap.getExaminee()))
                                    .and(QExamMap.examMap.exam.eq(examMap.getExam()))
                            );

                            if (findExamMap != null) examMap.set_id(findExamMap.get_id());

                            examMapRepository.save(examMap);
                        }
                        return true;
                    })
                    .toBlocking().first();
        }

    }

    public void saveItem(ApiService apiService, Set<String> examCdSet) {
        examCdSet.forEach(s -> {
            Observable.range(0, Integer.MAX_VALUE)
                    .concatMap(page -> apiService.item(new HashMap<>(), page, Integer.MAX_VALUE, null))
                    .takeUntil(page -> page.last)
                    .map(page -> {
                        Set<String> devis = new HashSet<>();

                        page.content.forEach(item -> {
                            itemRepository.save(item);
                            deviRepository.save(item.getDevi());

                            devis.add(item.getDevi().getDeviCd());
                        });
                        return devis;
                    })
                    .toBlocking().first();
        });
    }
}
