package com.humane.smps.repository;

import com.humane.smps.model.ExamHall;
import com.humane.util.spring.data.QueryDslJpaExtendRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamHallRepository extends QueryDslJpaExtendRepository<ExamHall, Long> {
}