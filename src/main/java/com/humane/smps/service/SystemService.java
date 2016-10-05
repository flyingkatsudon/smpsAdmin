package com.humane.smps.service;

import com.humane.smps.dto.DownloadWrapper;
import com.humane.smps.model.*;
import com.humane.smps.repository.*;
import com.humane.util.retrofit.QueryBuilder;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.hibernate.HibernateQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
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

import static com.humane.smps.model.QExamMap.examMap;

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
    private final HallDateRepository hallDateRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Value("${path.image.examinee:C:/api/image/examinee}") String pathExaminee;
    @Value("${path.smps.jpg:C:/api/smps/jpg}") String pathJpg;
    @Value("${path.smps.pdf:C:/api/smps/pdf}") String pathPdf;

    @Transactional
    public void resetData(boolean photo) throws IOException {
        HibernateQueryFactory queryFactory = new HibernateQueryFactory(entityManager.unwrap(Session.class));

        queryFactory.delete(QSheet.sheet).execute();
        queryFactory.delete(QScore.score).execute();
        queryFactory.delete(QScoreLog.scoreLog).execute();
        queryFactory.delete(QExamHallDate.examHallDate).execute();
        queryFactory.delete(QExamHall.examHall).execute();

        QExaminee examinee = QExaminee.examinee;

        ScrollableResults scrollableResults = queryFactory.select(examMap.examinee.examineeCd)
                .distinct()
                .from(examMap)
                .setFetchSize(Integer.MIN_VALUE)
                .scroll(ScrollMode.FORWARD_ONLY);

        while (scrollableResults.next()) {
            String examineeCd = scrollableResults.getString(0);
            queryFactory.delete(examMap).where(examMap.examinee.examineeCd.eq(examineeCd)).execute();
            try {
                queryFactory.delete(examinee).where(examinee.examineeCd.eq(examineeCd)).execute();
                if (photo) new File(pathExaminee, examineeCd + ".jpg").delete();

            } catch (Exception ignored) {
            }
        }
        scrollableResults.close();

        try {
            queryFactory.delete(QHall.hall).execute();
        } catch (Exception ignored) {
        }
        queryFactory.delete(QItem.item).execute();
        queryFactory.delete(QDevi.devi1).where(QDevi.devi1.devi.isNotNull()).execute();
        queryFactory.delete(QDevi.devi1).execute();


        QExam exam = QExam.exam;

        scrollableResults = queryFactory.select(exam.admission.admissionCd)
                .distinct()
                .from(exam)
                .setFetchSize(Integer.MIN_VALUE)
                .scroll(ScrollMode.FORWARD_ONLY);

        while (scrollableResults.next()) {
            String admissionCd = scrollableResults.getString(0);
            queryFactory.delete(exam).where(exam.admission.admissionCd.eq(admissionCd)).execute();

            try {
                queryFactory.delete(QAdmission.admission).where(QAdmission.admission.admissionCd.eq(admissionCd)).execute();
            } catch (Exception ignored) {
            }
        }

        scrollableResults.close();

        deleteFiles(pathJpg, pathPdf);

    }

    @Transactional
    public void initData() throws IOException {
        HibernateQueryFactory queryFactory = new HibernateQueryFactory(entityManager.unwrap(Session.class));

        queryFactory.delete(QSheet.sheet).execute();
        queryFactory.delete(QScoreLog.scoreLog).execute();
        queryFactory.delete(QScore.score).execute();

        QExamMap examMap = QExamMap.examMap;

        queryFactory.update(examMap)
                .setNull(examMap.virtNo)
                .setNull(examMap.scanDttm)
                .setNull(examMap.photoNm)
                .setNull(examMap.memo)
                .setNull(examMap.evalCd)
                .execute();

        deleteFiles(pathJpg, pathPdf);
    }

    public void deleteFiles(String... path) throws IOException {
        for (String p : path) {
            File folder = new File(p);
            File[] files = folder.listFiles();
            if (files != null)
                for (File file : files) {
                    file.delete();
                }
        }
    }

    public void saveExamMap(ApiService apiService, DownloadWrapper wrapper) {
        for (DownloadWrapper.ExamHallWrapper examHallWrapper : wrapper.getList()) {
            String newExamCd = examHallWrapper.getExamCd();
            String newHallCd = examHallWrapper.getHallCd();

            Observable.range(0, Integer.MAX_VALUE)
                    .concatMap(page -> apiService.examMap(new QueryBuilder().add("exam.examCd", newExamCd).add("hall.hallCd", newHallCd).getMap(), page, Integer.MAX_VALUE))
                    .takeUntil(page -> page.last)
                    .flatMap(page -> {
                        for (ExamMap examMap : page.content) {
                            admissionRepository.save(examMap.getExam().getAdmission());

                            Hall hall = examMap.getHall();
                            Exam exam = examMap.getExam();

                            hallRepository.save(examMap.getHall());
                            examRepository.save(examMap.getExam());

                            ExamHall findExamHall = examHallRepository.findOne(new BooleanBuilder()
                                    .and((QExamHall.examHall.exam.examCd.eq(exam.getExamCd())))
                                    .and(QExamHall.examHall.hall.hallCd.eq(hall.getHallCd()))
                            );

                            if (findExamHall == null) {
                                ExamHall examHall = new ExamHall();
                                examHall.setExam(exam);
                                examHall.setHall(hall);
                                examHallRepository.save(examHall);
                            }

                            ExamHallDate findExamHallDate = hallDateRepository.findOne(new BooleanBuilder()
                                    .and(QExamHallDate.examHallDate.exam.examCd.eq(exam.getExamCd()))
                                    .and(QExamHallDate.examHallDate.hall.hallCd.eq(hall.getHallCd()))
                                    .and(QExamHallDate.examHallDate.hallDate.eq(examMap.getHallDate()))
                            );

                            if (findExamHallDate == null) {
                                ExamHallDate examHallDate = new ExamHallDate();
                                examHallDate.setExam(exam);
                                examHallDate.setHall(hall);
                                examHallDate.setHallDate(examMap.getHallDate());
                                hallDateRepository.save(examHallDate);
                            }

                            examineeRepository.save(examMap.getExaminee());
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
        File path = new File(pathExaminee);

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
