package com.humane.smps.repository;

import com.humane.smps.model.ExamHallDate;
import com.humane.util.spring.data.QueryDslJpaExtendRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HallDateRepository extends QueryDslJpaExtendRepository<ExamHallDate, Long> {

}