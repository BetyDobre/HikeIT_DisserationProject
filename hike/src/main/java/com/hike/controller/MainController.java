package com.hike.controller;

import com.hike.dto.GrupaMuntoasaDto;
import com.hike.service.GrupaMuntoasaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MainController {
    private GrupaMuntoasaService grupaMuntoasaService;

    @Autowired
    public MainController(GrupaMuntoasaService grupaMuntoasaService) {
        this.grupaMuntoasaService = grupaMuntoasaService;
    }

    @GetMapping("/")
    public String mainPage(Model model)
    {
        List<GrupaMuntoasaDto> grupe = grupaMuntoasaService.findAllGroups();
        model.addAttribute("grupe", grupe);

        return "index";
    }

    @GetMapping("/contact")
    public String contactPage(Model model)
    {
        return "contact";
    }

    @GetMapping("/blog")
    public String blogPage(Model model)
    {
        return "blog";
    }

    @GetMapping("/404")
    public String errorPage(Model model) { return "404";}
}
