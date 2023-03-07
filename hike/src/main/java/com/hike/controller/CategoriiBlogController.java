package com.hike.controller;

import com.hike.dto.BlogCategoryDto;
import com.hike.dto.RegistrationDto;
import com.hike.models.BlogCategory;
import com.hike.service.BlogCategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/blog/categorii")
public class CategoriiBlogController {
    private BlogCategoryService blogCategoryService;

    @Autowired
    public CategoriiBlogController(BlogCategoryService blogCategoryService) {
        this.blogCategoryService = blogCategoryService;
    }

    @GetMapping("")
    public String getCategorii(Model model){
        model.addAttribute("categorii", blogCategoryService.getAllCategories());

        return "categoriiBlog";
    }

    @GetMapping("/adauga")
    public String adaugaCategorieForm(Model model){

        model.addAttribute("categorie", new BlogCategoryDto());
        return "categorieForm";
    }

    @PostMapping("/adauga")
    public String adaugaCategorie(@Valid @ModelAttribute("categorie") BlogCategoryDto categorie,
                                  BindingResult result, Model model){

        BlogCategory existingCategorie = blogCategoryService.findByTitlu(categorie.getTitlu().substring(0, 1).toUpperCase() + categorie.getTitlu().substring(1).toLowerCase());
        if(existingCategorie != null){
            result.rejectValue("titlu", "error.titlu","Există deja o categorie cu acest titlu.");
        }

        if(result.hasErrors()){
            model.addAttribute("categorie", categorie);
            return "categorieForm";
        }

        blogCategoryService.save(categorie);

        return "redirect:/blog/categorii?success";
    }

    @GetMapping("/{id}/sterge")
    public String stergeCategorie(@PathVariable Long id, Model model){
        blogCategoryService.delete(id);

        return "redirect:/blog/categorii?success";
    }
}
