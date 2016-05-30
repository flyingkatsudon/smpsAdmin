package com.humane.smps.repository;

import com.humane.smps.model.Examinee;
import com.humane.util.spring.data.QueryDslJpaExtendRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamineeRepository extends QueryDslJpaExtendRepository<Examinee, String> {

}