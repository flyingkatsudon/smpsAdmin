package com.humane.smps.repository;

import com.humane.smps.model.Score;
import com.humane.util.spring.data.QueryDslJpaExtendRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreRepository extends QueryDslJpaExtendRepository<Score, String> {
}