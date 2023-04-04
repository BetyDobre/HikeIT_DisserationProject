package com.hike.controller;

import com.hike.dto.TraseuDto;
import com.hike.models.UserEntity;
import com.hike.models.Utility;
import com.hike.service.GrupaMuntoasaService;
import com.hike.service.TraseuService;
import com.hike.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

@Controller
@RequestMapping("/trasee")
public class TraseuController {
    private TraseuService traseuService;
    private GrupaMuntoasaService grupaMuntoasaService;
    private UserService userService;

    @Autowired
    public TraseuController(TraseuService traseuService, GrupaMuntoasaService grupaMuntoasaService, UserService userService) {
        this.traseuService = traseuService;
        this.grupaMuntoasaService = grupaMuntoasaService;
        this.userService = userService;
    }

    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
            throws ServletException {

        // Convert multipart object to byte[]
        binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
    }

    @GetMapping("")
    public String getTrasee(){
        return "trasee";
    }

    @GetMapping("/adauga")
    public String adaugaTraseuNou(Model model) {
        TraseuDto traseuDto = new TraseuDto();
        model.addAttribute("traseu", traseuDto);
        model.addAttribute("grupeMuntoase", grupaMuntoasaService.findAllGroups());

        return "traseuForm";
    }

    @PostMapping("/adauga")
    public String adaugaTraseu(@Valid @ModelAttribute("traseu") TraseuDto traseuDto,
                               BindingResult result, Model model){
        if(result.hasErrors()){
            System.out.println(result.getAllErrors());

            model.addAttribute("traseu", traseuDto);
            model.addAttribute("grupeMuntoase", grupaMuntoasaService.findAllGroups());
            return "traseuForm";
        }

        String username = Utility.getLoggedUser();
        UserEntity user = userService.findByUsername(username);
        traseuDto.setUser(user);

        boolean isAdmin = user.getRoles().stream().anyMatch(role -> role.getName().equals("ADMIN"));
        if(isAdmin){
            traseuDto.setAprobat(true);
        }
        else{
            traseuDto.setAprobat(false);
        }

        traseuService.save(traseuDto);

        return "redirect:/trasee?adaugaSuccess";
    }
}
