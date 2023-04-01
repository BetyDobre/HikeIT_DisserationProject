package com.hike.repository;

import com.hike.models.BlogComment;
import com.hike.models.BlogPost;
import com.hike.models.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BlogCommentRepository extends JpaRepository<BlogComment, Long> {
    Page<BlogComment> findAllByPostare(BlogPost blogPost, Pageable pageable);

    @Query("SELECT count(c) from comentarii_blog c where c.postare.id = :postId")
    int noOfCommentsByPost(Long postId);

    int countAllByUser(UserEntity user);

    List<BlogComment> getBlogCommentsByPostare(BlogPost blogPost);
}
