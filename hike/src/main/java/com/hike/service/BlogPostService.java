package com.hike.service;

import com.hike.models.BlogPost;

import java.util.List;

public interface BlogPostService {
    List<BlogPost> findAllPosts();
}
