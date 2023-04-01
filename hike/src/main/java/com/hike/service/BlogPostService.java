package com.hike.service;

import com.hike.dto.BlogPostDto;
import com.hike.models.BlogCategory;
import com.hike.models.BlogComment;
import com.hike.models.BlogPost;
import com.hike.models.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface BlogPostService {
    List<BlogPost> findAll();
    Page<BlogPost> findAllPosts(Pageable pageable);
    List<Object[]> countPostsByCategory();
    BlogPost getById(Long id);
    Page<BlogPost> findByCategorie(BlogCategory category, Pageable pageable);
    Page<BlogPost> findByContainingTitlu(String titlu, Pageable pageable);
    Page<BlogPost> findByCategorieAndTitluContains(BlogCategory category, String titlu, Pageable pageable);
    void save(BlogPostDto blogPostDto);
    Page<BlogPost> findByUser(UserEntity user, Pageable pageable);
    void delete(Long id);
    List<BlogPost> findAllOrderByNumarComentariiDesc();
    int countAllByUser(UserEntity user);
}
