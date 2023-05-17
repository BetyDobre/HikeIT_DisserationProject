package com.hike.controller;

import com.hike.dto.BlogCommentDto;
import com.hike.dto.BlogPostDto;
import com.hike.models.*;
import com.hike.service.BlogCategoryService;
import com.hike.service.BlogCommentService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/blog")
public class BlogController {
    private BlogPostService blogPostService;
    private BlogCategoryService blogCategoryService;
    private UserService userService;
    private BlogCommentService blogCommentService;
    private final int pageSize = 5;

    @Autowired
    public BlogController(BlogPostService blogPostService, BlogCategoryService blogCategoryService, UserService userService, BlogCommentService blogCommentService) {
        this.blogPostService = blogPostService;
        this.blogCategoryService = blogCategoryService;
        this.userService = userService;
        this.blogCommentService = blogCommentService;
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

        List<BlogPost> postari = blogPostService.findAll();
        Map<Long, Integer> nrComentarii = new HashMap<>();
        for (BlogPost postare : postari) {
            Long postId = postare.getId();
            int totalComentarii = blogCommentService.noOfCommentsByPost(postId);
            nrComentarii.put(postId, totalComentarii);
        }

        List<BlogPost> postariPopulare = blogPostService.findAllOrderByNumarComentariiDesc();
        if (postariPopulare.size() < 5){
            model.addAttribute("postariPopulare", postariPopulare);
        }
        else {
            model.addAttribute("postariPopulare", postariPopulare.subList(0, 5));
        }

        model.addAttribute("nrComentarii", nrComentarii);
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

        postare.setText(postare.getText().replaceAll("\n", "<br>"));
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

//    comentarii si single post
    void addCommonAttributesBlogPost(Model model, int pageNo, Long postId){
        String username = Utility.getLoggedUser();
        UserEntity user = userService.findByUsername(username);
        if(user != null){
            model.addAttribute("userId", user.getId());
        }
        else {
            model.addAttribute("userId", null);
        }
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("categorii", blogPostService.countPostsByCategory());
        model.addAttribute("all", blogPostService.findAllPosts(PageRequest.of(pageNo - 1, pageSize)).getTotalElements());

        BlogPost blogPost= blogPostService.getById(postId);
        model.addAttribute("postare", blogPost);
        model.addAttribute("comentarii", blogCommentService.getAllCommentsByPost(blogPost, PageRequest.of(pageNo - 1, pageSize)));
        model.addAttribute("link", "/blog/" + postId);
        model.addAttribute("nrComentarii", blogCommentService.noOfCommentsByPost(postId));

        List<BlogPost> postariPopulare = blogPostService.findAllOrderByNumarComentariiDesc();
        if (postariPopulare.size() < 5){
            model.addAttribute("postariPopulare", postariPopulare);
        }
        else {
            model.addAttribute("postariPopulare", postariPopulare.subList(0, 5));
        }

    }

    @GetMapping("/{id}")
    public String getSinglePost(@RequestParam(required = false) String search, Model model, @PathVariable("id") Long id, @RequestParam(value = "page", defaultValue = "1", required = false) int pageNo){
        model.addAttribute("comentariuNou", new BlogCommentDto());
        addCommonAttributesBlogPost(model, pageNo, id);

        if(search != null){
            return "redirect:/blog?search=" + search;
        }

        return "blogPost";
    }

    @PostMapping("/{id}")
    public String adaugaComentariu(Model model, @PathVariable("id") Long id,
                                   @ModelAttribute("comentariuNou") BlogCommentDto blogCommentDto,
                                   BindingResult result,
                                   @RequestParam(value = "page", defaultValue = "1", required = false) int pageNo){
        if(result.hasErrors()){
            System.out.println(result.getAllErrors());

            model.addAttribute("comentariuNou", blogCommentDto);
            addCommonAttributesBlogPost(model, pageNo, id);
            return "blogForm";
        }
        String username = Utility.getLoggedUser();
        UserEntity user = userService.findByUsername(username);
        blogCommentDto.setPostare(blogPostService.getById(id));
        blogCommentDto.setUser(user);

        addCommonAttributesBlogPost(model, pageNo, id);
        blogCommentService.save(blogCommentDto);

        return "redirect:/blog/" + id +"?adaugaSucces";
    }

    @GetMapping("/{postId}/{commId}/sterge")
    public String stergeComentariu(Model model, @PathVariable("postId") Long postId, @PathVariable("commId") Long commId,
                                   @RequestParam(value = "page", defaultValue = "1", required = false) int pageNo){
        blogCommentService.delete(commId);
        addCommonAttributesBlogPost(model, pageNo, postId);

        return "redirect:/blog/" + postId +"?stergeSucces";
    }

}
