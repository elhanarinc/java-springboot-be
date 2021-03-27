package com.challenge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.challenge.model.Item;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
  @Query(value = "SELECT * FROM item ORDER BY ts_create ASC LIMIT ?1 OFFSET ?2",
          nativeQuery = true)
  List<Item> findPaginatedItems(Integer limit, Integer offset);
}

