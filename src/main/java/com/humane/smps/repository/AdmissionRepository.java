package com.humane.smps.repository;

import com.humane.smps.model.Admission;
import com.humane.util.spring.data.QueryDslJpaExtendRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdmissionRepository extends QueryDslJpaExtendRepository<Admission, String> {
}