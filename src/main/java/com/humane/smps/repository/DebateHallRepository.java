package com.humane.smps.repository;

import com.humane.smps.model.ExamDebateHall;
import com.humane.util.spring.data.QueryDslJpaExtendRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DebateHallRepository extends QueryDslJpaExtendRepository<ExamDebateHall, Long> {

}