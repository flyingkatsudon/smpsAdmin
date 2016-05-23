package com.humane.smps.repository;

import com.humane.smps.model.Devi;
import com.humane.util.spring.data.QueryDslJpaExtendRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviRepository extends QueryDslJpaExtendRepository<Devi, String> {
}