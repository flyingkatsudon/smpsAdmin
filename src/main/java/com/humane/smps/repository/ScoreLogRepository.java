package com.humane.smps.repository;

import com.humane.smps.model.ScoreLog;
import com.humane.util.spring.data.QueryDslJpaExtendRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreLogRepository extends QueryDslJpaExtendRepository<ScoreLog, Long> {
}