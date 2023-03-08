package com.hike.service;

import com.hike.models.BlogCategory;
import com.hike.models.BlogPost;

import java.util.List;
import java.util.Map;

public interface BlogPostService {
    List<BlogPost> findAllPosts();
    List<Object[]> countPostsByCategory();
    BlogPost getById(Long id);
    List<BlogPost> findByCategorie(BlogCategory category);
}
