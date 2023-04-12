package com.hike.repository;

import com.hike.models.Traseu;
import com.hike.models.TraseuComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TraseuCommentRepository extends JpaRepository<TraseuComment, Long> {
    Page<TraseuComment> findAllByTraseu(Traseu traseu, Pageable pageable);
    @Query("SELECT count(c) from comentarii_traseu c where c.traseu.id = :traseuId")
    int noOfCommentsByTraseu(Long traseuId);
}
