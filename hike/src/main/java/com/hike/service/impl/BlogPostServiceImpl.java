package com.hike.service.impl;

import com.hike.dto.BlogPostDto;
import com.hike.models.BlogCategory;
import com.hike.models.BlogPost;
import com.hike.repository.BlogPostRepository;
import com.hike.service.BlogPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BlogPostServiceImpl implements BlogPostService {

    private BlogPostRepository blogPostRepository;

    @Autowired
    public BlogPostServiceImpl(BlogPostRepository blogPostRepository) {
        this.blogPostRepository = blogPostRepository;
    }

    @Override
    public List<BlogPost> findAllPosts() {
        return blogPostRepository.findAll();
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
}
