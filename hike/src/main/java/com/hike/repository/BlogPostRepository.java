package com.hike.repository;

import com.hike.models.BlogCategory;
import com.hike.models.BlogPost;
import com.hike.models.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
    List<BlogPost> findAll();

    @Query("SELECT c.titlu, COALESCE((SELECT COUNT(*) FROM postari_blog b WHERE b.categorie.id=c.id), 0) FROM categorii_blog c")
    List<Object[]> countPostsByCategory();

    BlogPost getBlogPostById(Long id);

    Page<BlogPost> findByCategorie(BlogCategory blogCategory, Pageable pageable);

    Page<BlogPost> findByTitluContains(String titlu, Pageable pageable);

    Page<BlogPost> findByCategorieAndTitluContains(BlogCategory category, String titlu, Pageable pageable);

    Page<BlogPost> findByUser(UserEntity user, Pageable pageable);
}
