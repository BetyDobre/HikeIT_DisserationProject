package com.hike.service.impl;

import com.hike.dto.BlogCategoryDto;
import com.hike.models.BlogCategory;
import com.hike.repository.BlogCategoryRepository;
import com.hike.service.BlogCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class BlogCategoryServiceImpl implements BlogCategoryService {
    private BlogCategoryRepository blogCategoryRepository;

    @Autowired
    public BlogCategoryServiceImpl(BlogCategoryRepository blogCategoryRepository) {
        this.blogCategoryRepository = blogCategoryRepository;
    }

    @Override
    public List<BlogCategory> getAllCategories() {
        return  blogCategoryRepository.findAll();
    }

    @Override
    public BlogCategory findByTitlu(String titlu) {
        return blogCategoryRepository.findByTitlu(titlu);
    }

    @Override
    public void save(BlogCategoryDto blogCategoryDto) {
        BlogCategory blogCategory = new BlogCategory();
        blogCategory.setId(blogCategoryDto.getId());
        blogCategory.setTitlu(blogCategoryDto.getTitlu().substring(0, 1).toUpperCase() + blogCategoryDto.getTitlu().substring(1).toLowerCase());

        blogCategoryRepository.save(blogCategory);
    }

    @Override
    public void delete(Long id) {
        blogCategoryRepository.deleteById(id);
    }

    @Override
    public Optional<BlogCategory> findById(Long id) {
        return blogCategoryRepository.findById(id);
    }
}
