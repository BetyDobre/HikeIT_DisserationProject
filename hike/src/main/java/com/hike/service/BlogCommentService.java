package com.hike.service;

import com.hike.dto.BlogCommentDto;
import com.hike.models.BlogComment;
import com.hike.models.BlogPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BlogCommentService {
    Page<BlogComment> getAllCommentsByPost(BlogPost blogPost, Pageable pageable);
    void save(BlogCommentDto blogCommentDto);
    int noOfCommentsByPost(Long id);
    void delete(Long id);
}
