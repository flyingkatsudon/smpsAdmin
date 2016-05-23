package com.humane.smps.repository;

import com.humane.smps.model.Item;
import com.humane.util.spring.data.QueryDslJpaExtendRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends QueryDslJpaExtendRepository<Item, String> {
}