package com.hike.controller;

import com.hike.models.BlogCategory;
import com.hike.service.BlogCategoryService;
import com.hike.service.BlogPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/blog")
public class BlogController {
    private BlogPostService blogPostService;
    private BlogCategoryService blogCategoryService;

    @Autowired
    public BlogController(BlogPostService blogPostService, BlogCategoryService blogCategoryService) {
        this.blogPostService = blogPostService;
        this.blogCategoryService = blogCategoryService;
    }

    @GetMapping("")
    public String getAllBlogPosts(Model model){
        model.addAttribute("postari", blogPostService.findAllPosts());

        model.addAttribute("categorii", blogPostService.countPostsByCategory());
        model.addAttribute("all", blogPostService.findAllPosts().size());

        return "blog";
    }

    @GetMapping("/categorie/{categorieTitlu}")
    public String getAllByCategory(@PathVariable String categorieTitlu, Model model){
        BlogCategory categorie = blogCategoryService.findByTitlu(categorieTitlu);
        model.addAttribute("postari", blogPostService.findByCategorie(categorie));

        model.addAttribute("categorii", blogPostService.countPostsByCategory());
        model.addAttribute("all", blogPostService.findAllPosts().size());

        return "blog";
    }

}
