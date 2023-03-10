package com.hike.service.impl;

import com.hike.models.BlogCategory;
import com.hike.models.BlogPost;
import com.hike.repository.BlogPostRepository;
import com.hike.service.BlogPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class BlogPostServiceImpl implements BlogPostService {

    private BlogPostRepository blogPostRepository;

    @Autowired
    public BlogPostServiceImpl(BlogPostRepository blogPostRepository) {
        this.blogPostRepository = blogPostRepository;
    }

    @Override
    public Page<BlogPost> findAllPosts(Pageable pageable) {
        return blogPostRepository.findAll(pageable);
    }

    @Override
    public List<Object[]> countPostsByCategory() {
        return blogPostRepository.countPostsByCategory();
    }

    @Override
    public BlogPost getById(Long id) {
        return blogPostRepository.getBlogPostById(id);
    }

    @Override
    public List<BlogPost> findByCategorie(BlogCategory categorie) {
        return blogPostRepository.findByCategorie(categorie);
    }

    @Override
    public Page<BlogPost> findByContainingTitlu(String titlu, Pageable pageable) {
        return blogPostRepository.findByTitluContains(titlu, pageable);
    }
}
