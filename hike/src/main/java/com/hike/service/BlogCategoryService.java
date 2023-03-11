package com.hike.service;

import com.hike.dto.BlogCategoryDto;
import com.hike.models.BlogCategory;

import java.util.List;
import java.util.Optional;

public interface BlogCategoryService {
    List<BlogCategory> getAllCategories();
    BlogCategory findByTitlu(String titlu);
    void save(BlogCategoryDto blogCategoryDto);
    void delete(Long id);
    Optional<BlogCategory> findById(Long id);
}
