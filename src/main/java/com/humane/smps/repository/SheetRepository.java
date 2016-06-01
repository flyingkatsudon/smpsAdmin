package com.humane.smps.repository;

import com.humane.smps.model.Sheet;
import com.humane.util.spring.data.QueryDslJpaExtendRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SheetRepository extends QueryDslJpaExtendRepository<Sheet, Long> {
}