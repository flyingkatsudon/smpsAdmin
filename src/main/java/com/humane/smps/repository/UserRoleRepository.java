package com.humane.smps.repository;

import com.humane.smps.model.UserRole;
import com.humane.util.spring.data.QueryDslJpaExtendRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends QueryDslJpaExtendRepository<UserRole, String> {
}