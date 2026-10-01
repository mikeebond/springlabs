package com.kpi.io45.bondarchuk.repository;

import com.kpi.io45.bondarchuk.model.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {

    // Auto-generated method by Spring Data JPA
    Optional<Category> findByName(String name);
}