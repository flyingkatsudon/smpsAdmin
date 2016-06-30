package com.humane.smps.service;

import com.humane.smps.dto.DownloadWrapper;
import com.humane.smps.model.*;
import com.humane.smps.repository.*;
import com.humane.util.retrofit.QueryBuilder;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.hibernate.HibernateDeleteClause;
import com.mysema.query.jpa.hibernate.HibernateQuery;
import com.mysema.query.jpa.hibernate.HibernateUpdateClause;
import com.mysema.query.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rx.Observable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    @Value("${path.image.examinee:C:/api/image/examinee}") String pathImageExaminee;

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


        /**
         * SELECT DISTINCT ADMISSION_CD
         *   FROM ADMISSION
         *  INNER JOIN EXAM ON ADMISSION.ADMISSION_CD = EXAM.ADMISSION_CD
         */

        List<String> admissions = new JPAQuery(entityManager)
                .from(QExam.exam)
                .distinct()
                .list(QExam.exam.admission.admissionCd);

        new HibernateDeleteClause(entityManager.unwrap(Session.class), QExam.exam).execute();
        new HibernateDeleteClause(entityManager.unwrap(Session.class), QAdmission.admission)
                .where(QAdmission.admission.admissionCd.in(admissions))
                .execute();
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

    public void saveExamMap(ApiService apiService, DownloadWrapper wrapper) {
        for (DownloadWrapper.ExamHallWrapper examHallWrapper : wrapper.getList()) {
            String newExamCd = examHallWrapper.getExamCd();
            String newHallCd = examHallWrapper.getHallCd();
            Observable.range(0, Integer.MAX_VALUE)
                    .concatMap(page -> apiService.examMap(new QueryBuilder().add("exam.examCd", newExamCd).add("hall.hallCd", newHallCd).getMap(), page, Integer.MAX_VALUE, null))
                    .takeUntil(page -> page.last)
                    .flatMap(page -> {
                        String examCd = null;
                        String hallCd = null;
                        for (ExamMap examMap : page.content) {
                            admissionRepository.save(examMap.getExam().getAdmission());
                            examRepository.save(examMap.getExam());
                            hallRepository.save(examMap.getHall());

                            // examHall이 없으면 생성
                            if(!StringUtils.equals(examCd, examMap.getExam().getExamCd())
                                    || !StringUtils.equals(hallCd, examMap.getHall().getHallCd())) {
                                examCd = examMap.getExam().getExamCd();
                                hallCd = examMap.getHall().getHallCd();

                                ExamHall examHall = new ExamHall();
                                examHall.setExam(examMap.getExam());
                                examHall.setHall(examMap.getHall());

                                ExamHall findExamHall = examHallRepository.findOne(new BooleanBuilder()
                                        .and(QExamHall.examHall.exam.eq(examMap.getExam()))
                                        .and(QExamHall.examHall.hall.eq(examMap.getHall()))
                                );

                                if (findExamHall != null) examHall.set_id(findExamHall.get_id());
                                examHallRepository.save(examHall);
                            }

                            examineeRepository.save(examMap.getExaminee());

                            ExamMap findExamMap = examMapRepository.findOne(new BooleanBuilder()
                                    .and(QExamMap.examMap.examinee.eq(examMap.getExaminee()))
                                    .and(QExamMap.examMap.exam.eq(examMap.getExam()))
                            );

                            if (findExamMap != null) examMap.set_id(findExamMap.get_id());

                            examMapRepository.save(examMap);
                        }
                        return Observable.from(page.content);
                    })
                    .flatMap(examMap -> Observable.just(examMap.getExaminee().getExamineeCd() + ".jpg"))
                    .flatMap(fileName -> imageExaminee(apiService, fileName))
                    .reduce(new ArrayList<>(), (list, file) -> list)
                    .toBlocking().first();
        }
    }

    private Observable<File> imageExaminee(ApiService apiService, String fileName) {
        File path = new File(pathImageExaminee);

        if (!path.exists()) path.mkdirs();
        File file = new File(path, fileName);

        if (file.exists()) {
            return Observable.just(file);
        } else {
            return apiService.imageExaminee(fileName)
                    .flatMap(responseBody -> {
                        try (FileOutputStream fos = new FileOutputStream(file)) {
                            IOUtils.write(responseBody.bytes(), fos);
                            return Observable.just(file);
                        } catch (IOException e) {
                            return Observable.error(e);
                        }
                    });
        }
    }

    public void saveItem(ApiService apiService, DownloadWrapper wrapper) {
        List<String> examCds = new ArrayList<>();
        wrapper.getList().forEach(examHallWrapper -> {
            if (!examCds.contains(examHallWrapper.getExamCd()))
                examCds.add(examHallWrapper.getExamCd());
        });

        List<String> deviList = new ArrayList<>();

        examCds.forEach(examCd -> {
            Observable.range(0, Integer.MAX_VALUE)
                    .concatMap(page -> apiService.item(new QueryBuilder().add("exam.examCd", examCd).getMap(), page, Integer.MAX_VALUE, null))
                    .takeUntil(page -> page.last)
                    .map(page -> {
                        page.content.forEach(item -> {
                            deviRepository.save(item.getDevi());
                            examRepository.save(item.getExam());

                            Item findItem = itemRepository.findOne(new BooleanBuilder()
                                    .and(QItem.item.exam.eq(item.getExam()))
                                    .and(QItem.item.itemNo.eq(item.getItemNo()))
                            );

                            log.debug("{}", item);
                            log.debug("{}", findItem);

                            if (findItem != null) item.set_id(findItem.get_id());

                            itemRepository.save(item);

                            if (!deviList.contains(item.getDevi().getDeviCd()))
                                deviList.add(item.getDevi().getDeviCd());
                        });
                        return null;
                    })
                    .toBlocking().first();
        });

        deviList.forEach(deviCd -> {
            Observable.range(0, Integer.MAX_VALUE)
                    .concatMap(page -> apiService.devi(new QueryBuilder().add("devi.deviCd", deviCd).getMap(), page, Integer.MAX_VALUE, null))
                    .takeUntil(page -> page.last)
                    .map(page -> {
                        deviRepository.save(page.content);
                        return null;
                    })
                    .toBlocking().first();
        });
    }
}
