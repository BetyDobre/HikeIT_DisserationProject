package com.hike.repository;

import com.hike.models.BlogCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlogCategoryRepository extends JpaRepository<BlogCategory, Long> {
    List<BlogCategory> findAll();
    BlogCategory findByTitlu(String titlu);
    void deleteById(Long id);
}
