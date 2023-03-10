package com.hike.repository;

import com.hike.models.BlogCategory;
import com.hike.models.BlogPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
    List<BlogPost> findAll();

    @Query("SELECT c.titlu, COALESCE((SELECT COUNT(*) FROM postari_blog b WHERE b.categorie.id=c.id), 0) FROM categorii_blog c")
    List<Object[]> countPostsByCategory();

    BlogPost getBlogPostById(Long id);

    List<BlogPost> findByCategorie(BlogCategory blogCategory);

    Page<BlogPost> findByTitluContains(String titlu, Pageable pageable);
}
