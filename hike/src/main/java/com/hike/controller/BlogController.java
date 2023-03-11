package com.hike.controller;

import com.hike.dto.BlogPostDto;
import com.hike.models.BlogCategory;
import com.hike.models.BlogPost;
import com.hike.models.UserEntity;
import com.hike.models.Utility;
import com.hike.service.BlogCategoryService;
import com.hike.service.BlogPostService;
import com.hike.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/blog")
public class BlogController {
    private BlogPostService blogPostService;
    private BlogCategoryService blogCategoryService;
    private UserService userService;
    private final int pageSize = 2;

    @Autowired
    public BlogController(BlogPostService blogPostService, BlogCategoryService blogCategoryService, UserService userService) {
        this.blogPostService = blogPostService;
        this.blogCategoryService = blogCategoryService;
        this.userService = userService;
    }

    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
            throws ServletException {

        // Convert multipart object to byte[]
        binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
    }

    void addCommonAttributes(Model model, int pageNo){
        String username = Utility.getLoggedUser();
        UserEntity user = userService.findByUsername(username);
        if(user != null){
            model.addAttribute("userId", user.getId());
            boolean isBlogger = user.getRoles().stream().anyMatch(role -> role.getName().equals("BLOGGER"));
            if(isBlogger){
                model.addAttribute("access", "Adaugă postare");
            }
            else{
                model.addAttribute("access", null);
            }
        }
        else{
            model.addAttribute("userId", null);
            model.addAttribute("access", null);
        }

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("categorii", blogPostService.countPostsByCategory());
        model.addAttribute("all", blogPostService.findAllPosts(PageRequest.of(pageNo - 1, pageSize)).getTotalElements());
    }

    @GetMapping("")
    public String getAllBlogPosts(Model model, @RequestParam(required = false) String search,
                                  @RequestParam(value = "page", defaultValue = "1", required = false) int pageNo){
        if(search == null || search.equals("")){
            Page<BlogPost> postari = blogPostService.findAllPosts(PageRequest.of(pageNo-1, pageSize));
            model.addAttribute("postari", postari);
        }
        else{
            model.addAttribute("postari", blogPostService.findByContainingTitlu(search, PageRequest.of(pageNo-1, pageSize)));
        }

        model.addAttribute("link", "/blog");
        addCommonAttributes(model, pageNo);

        return "blog";
    }

    @GetMapping("/categorie/{categorieTitlu}")
    public String getAllByCategory(@PathVariable String categorieTitlu, Model model, @RequestParam(required = false) String search,
                                   @RequestParam(value = "page", defaultValue = "1", required = false) int pageNo){

        BlogCategory categorie = blogCategoryService.findByTitlu(categorieTitlu);
        if(search == null || search.equals("")) {
            model.addAttribute("postari", blogPostService.findByCategorie(categorie, PageRequest.of(pageNo - 1, pageSize)));
        }
        else{
            model.addAttribute("postari", blogPostService.findByCategorieAndTitluContains(categorie, search, PageRequest.of(pageNo - 1, pageSize)));
        }

        model.addAttribute("link", "/blog/categorie/" + categorieTitlu);
        addCommonAttributes(model, pageNo);

        return "blog";
    }

    @GetMapping("/adauga")
    public String adaugaPostare(Model model){
        model.addAttribute("postare", new BlogPostDto());
        model.addAttribute("categorii", blogCategoryService.getAllCategories());

        return "blogForm";
    }

    @PostMapping("/adauga")
    public String adaugaPostare(@Valid @ModelAttribute("postare") BlogPostDto postare,
                           BindingResult result, Model model,
                           final @RequestParam("pozaCoperta") MultipartFile file){
        if(result.hasErrors()){
            System.out.println(result.getAllErrors());

            model.addAttribute("postare", postare);
            model.addAttribute("categorii", blogCategoryService.getAllCategories());
            return "blogForm";
        }

        String username = Utility.getLoggedUser();
        UserEntity user = userService.findByUsername(username);
        postare.setUser(user);

        try {
            byte[] byteObjects = file.getBytes();
            postare.setPozaCoperta(byteObjects);
        } catch (IOException e) {
            System.out.println("Nu s-a putut seta imaginea pentru postare: " + e.getMessage());
        }

        blogPostService.save(postare);
        addCommonAttributes(model, 1);

        return "redirect:/blog?adaugaSuccess";
    }

    @GetMapping("/postarilemele")
    public String getPostariUserConectat(Model model, @RequestParam(value = "page", defaultValue = "1", required = false) int pageNo){
        String username = Utility.getLoggedUser();
        UserEntity user = userService.findByUsername(username);

        model.addAttribute("postari", blogPostService.findByUser(user, PageRequest.of(pageNo - 1, pageSize)));
        addCommonAttributes(model, pageNo);
        model.addAttribute("link", "/blog/postarilemele");

        return "blog";
    }

    @GetMapping("/{id}/sterge")
    public String stergePostare(Model model, @PathVariable("id") Long id){
        blogPostService.delete(id);

        return "redirect:/blog?stergeSuccess";
    }
}
