package com.humane.smps.repository;

import com.humane.smps.model.UserAdmission;
import com.humane.util.spring.data.QueryDslJpaExtendRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAdmissionRepository extends QueryDslJpaExtendRepository<UserAdmission, String> {
}