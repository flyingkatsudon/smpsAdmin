package com.humane.smps.repository;

import com.humane.smps.model.Exam;
import com.humane.util.spring.data.QueryDslJpaExtendRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamRepository extends QueryDslJpaExtendRepository<Exam, String> {
}