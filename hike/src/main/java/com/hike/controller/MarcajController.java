package com.hike.controller;

import com.hike.dto.MarcajDto;
import com.hike.models.Marcaj;
import com.hike.service.MarcajService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

@Controller
@RequestMapping("/marcaje")
public class MarcajController {

    private MarcajService marcajService;

    @Autowired
    public MarcajController(MarcajService marcajService) {
        this.marcajService = marcajService;
    }

    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
            throws ServletException {

        // Convert multipart object to byte[]
        binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
    }

    @GetMapping
    public String getMarcaje(Model model){
        model.addAttribute("marcaje", marcajService.getAllMarcaje());

        return "marcaje";
    }

    @GetMapping("/adauga")
    public String adaugaMarcaj(Model model){
        model.addAttribute("marcaj", new MarcajDto());
        return "marcajForm";
    }

    @PostMapping("/adauga")
    public String adaugaMarcaj(@Valid @ModelAttribute("marcaj") MarcajDto marcajDto,
                                  BindingResult result, Model model){

        Marcaj existingMarcaj = marcajService.findByTitlu(marcajDto.getTitlu());
        if(existingMarcaj!= null){
            result.rejectValue("marcaj", "error.titlu","Există deja un marcaj cu acest titlu.");
        }

        if(result.hasErrors()){
            model.addAttribute("marcaj", marcajDto);
            return "marcajForm";
        }

        marcajService.save(marcajDto);

        return "redirect:/marcaje?success";
    }

    @GetMapping("/{id}/sterge")
    public String stergeMarcaj(@PathVariable Long id, Model model){
        marcajService.delete(id);

        return "redirect:/marcaje?success";
    }
}
