package com.hike.service;

import com.hike.dto.BlogCommentDto;
import com.hike.models.BlogComment;
import com.hike.models.BlogPost;
import com.hike.models.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BlogCommentService {
    Page<BlogComment> getAllCommentsByPost(BlogPost blogPost, Pageable pageable);
    void save(BlogCommentDto blogCommentDto);
    void save(BlogComment blogComment);
    int noOfCommentsByPost(Long id);
    void delete(Long id);
    int countAllByUser(UserEntity user);
    List<BlogComment> getAllCommentsByPost(BlogPost blogPost);
}
