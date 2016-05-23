package com.humane.smps.repository;

import com.humane.smps.model.ExamMap;
import com.humane.util.spring.data.QueryDslJpaExtendRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamMapRepository extends QueryDslJpaExtendRepository<ExamMap, Long> {

}