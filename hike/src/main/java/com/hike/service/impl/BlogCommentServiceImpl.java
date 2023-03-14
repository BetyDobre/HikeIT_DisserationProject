package com.hike.service.impl;

import com.hike.dto.BlogCommentDto;
import com.hike.models.BlogComment;
import com.hike.models.BlogPost;
import com.hike.repository.BlogCommentRepository;
import com.hike.service.BlogCommentService;
import com.hike.service.BlogPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BlogCommentServiceImpl implements BlogCommentService {
    private BlogPostService blogPostService;
    private BlogCommentRepository blogCommentRepository;

    @Autowired
    public BlogCommentServiceImpl(BlogPostService blogPostService, BlogCommentRepository blogCommentRepository) {
        this.blogPostService = blogPostService;
        this.blogCommentRepository = blogCommentRepository;
    }

    @Override
    public Page<BlogComment> getAllCommentsByPost(BlogPost blogPost, Pageable pageable) {
        return blogCommentRepository.findAllByPostare(blogPost, pageable);
    }

    @Override
    public void save(BlogCommentDto blogCommentDto) {
        BlogComment blogComment = BlogComment.builder()
                .text(blogCommentDto.getText())
                .createdOn(blogCommentDto.getCreatedOn())
                .postare(blogCommentDto.getPostare())
                .user(blogCommentDto.getUser())
                .build();

        blogCommentRepository.save(blogComment);
    }

    @Override
    public int noOfCommentsByPost(Long id) {
        return blogCommentRepository.noOfCommentsByPost(id);
    }

    @Override
    public void delete(Long id) {
        blogCommentRepository.deleteById(id);
    }
}
