package com.hike.service.impl;

import com.hike.dto.BlogPostDto;
import com.hike.exception.ObjectNotFoundException;
import com.hike.models.*;
import com.hike.repository.BlogCategoryRepository;
import com.hike.repository.BlogCommentRepository;
import com.hike.repository.BlogPostRepository;
import com.hike.service.BlogCommentService;
import com.hike.service.BlogPostService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class BlogPostServiceImpl implements BlogPostService {

    private BlogPostRepository blogPostRepository;
    private BlogCategoryRepository blogCategoryRepository;
    private BlogCommentService blogCommentService;

    @Lazy
    @Autowired
    public BlogPostServiceImpl(BlogPostRepository blogPostRepository, BlogCategoryRepository blogCategoryRepository, BlogCommentService blogCommentService) {
        this.blogPostRepository = blogPostRepository;
        this.blogCategoryRepository = blogCategoryRepository;
        this.blogCommentService = blogCommentService;
    }

    @Override
    public List<BlogPost> findAll() {
        return blogPostRepository.findAll();
    }

    @Override
    public Page<BlogPost> findAllPosts(Pageable pageable) {
        return blogPostRepository.findAll(pageable);
    }

    @Override
    public List<Object[]> countPostsByCategory() {
        return blogPostRepository.countPostsByCategory();
    }

    @Override
    public BlogPost getById(Long id) {
        return blogPostRepository.getBlogPostById(id);
    }

    @Override
    public Page<BlogPost> findByCategorie(BlogCategory categorie, Pageable pageable) {
        return blogPostRepository.findByCategorie(categorie, pageable);
    }

    @Override
    public Page<BlogPost> findByContainingTitlu(String titlu, Pageable pageable) {
        return blogPostRepository.findByTitluContains(titlu, pageable);
    }

    @Override
    public Page<BlogPost> findByCategorieAndTitluContains(BlogCategory category, String titlu, Pageable pageable) {
        return blogPostRepository.findByCategorieAndTitluContains(category, titlu, pageable);
    }

    @Override
    public void save(BlogPostDto blogPostDto) {
        BlogPost postare = new BlogPost();

        postare.setTitlu(blogPostDto.getTitlu());
        postare.setDescriere(blogPostDto.getDescriere());
        postare.setText(blogPostDto.getText());
        postare.setUser(blogPostDto.getUser());
        postare.setPozaCoperta(blogPostDto.getPozaCoperta());
        Optional<BlogCategory> categorie = blogCategoryRepository.findById(blogPostDto.getCategorieId());
        postare.setCategorie(categorie.get());

        blogPostRepository.save(postare);
    }

    @Override
    public Page<BlogPost> findByUser(UserEntity user, Pageable pageable) {
        return blogPostRepository.findByUser(user, pageable);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        try {
            BlogPost postare = blogPostRepository.findById(id)
                    .orElseThrow(() -> new ObjectNotFoundException("Nu s-a gasit postarea cu ID-ul " + id));

            List<BlogComment> comentarii = blogCommentService.getAllCommentsByPost(postare);
            for (BlogComment comentariu : comentarii) {
                blogCommentService.delete(comentariu.getId());
            }
        } catch (Exception ex) {
            throw new RuntimeException("Nu s-a putut sterge postarea cu ID-ul " + id, ex);
        }

        blogPostRepository.deleteById(id);
    }

    @Override
    public List<BlogPost> findAllOrderByNumarComentariiDesc() {
        return blogPostRepository.findAllOrderByNumarComentariiDesc();
    }

    @Override
    public int countAllByUser(UserEntity user) {
        return blogPostRepository.countAllByUser(user);
    }
}
