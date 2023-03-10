package com.hike.controller;

import com.hike.models.BlogCategory;
import com.hike.models.BlogPost;
import com.hike.service.BlogCategoryService;
import com.hike.service.BlogPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/blog")
public class BlogController {
    private BlogPostService blogPostService;
    private BlogCategoryService blogCategoryService;
    private final int size = 2;

    @Autowired
    public BlogController(BlogPostService blogPostService, BlogCategoryService blogCategoryService) {
        this.blogPostService = blogPostService;
        this.blogCategoryService = blogCategoryService;
    }

    @GetMapping("")
    public String getAllBlogPosts(Model model, @RequestParam(required = false) String search,
                                  @RequestParam(value = "page", defaultValue = "1", required = false) int pageNo){
        if(search == null || search.equals("")){
            Page<BlogPost> postari = blogPostService.findAllPosts(PageRequest.of(pageNo-1, size));
            model.addAttribute("postari", postari);
        }
        else{
            model.addAttribute("postari", blogPostService.findByContainingTitlu(search, PageRequest.of(pageNo-1, size)));
        }

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("categorii", blogPostService.countPostsByCategory());
        model.addAttribute("all", blogPostService.findAllPosts(PageRequest.of(pageNo - 1, size)).getTotalElements());

        return "blog";
    }

    @GetMapping("/categorie/{categorieTitlu}")
    public String getAllByCategory(@PathVariable String categorieTitlu, Model model,
                                   @RequestParam(value = "page", defaultValue = "1", required = false) int pageNo){

        BlogCategory categorie = blogCategoryService.findByTitlu(categorieTitlu);
        model.addAttribute("postari", blogPostService.findByCategorie(categorie));
        model.addAttribute("paging", false);
        model.addAttribute("categorii", blogPostService.countPostsByCategory());
        model.addAttribute("all", blogPostService.findAllPosts(PageRequest.of(pageNo - 1, size)).getTotalElements());

        return "blog";
    }

}
