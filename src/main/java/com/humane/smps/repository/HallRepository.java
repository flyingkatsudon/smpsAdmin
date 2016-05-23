package com.humane.smps.repository;

import com.humane.smps.model.Hall;
import com.humane.util.spring.data.QueryDslJpaExtendRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HallRepository extends QueryDslJpaExtendRepository<Hall, String> {
}