package com.hike.controller;

import com.hike.dto.RegistrationDto;
import com.hike.models.UserEntity;
import com.hike.service.ImageService;
import com.hike.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class AuthController {
    private final UserService userService;
    private final ImageService imageService;

    public AuthController(UserService userService, ImageService imageService){
        this.userService = userService;
        this.imageService = imageService;
    }

    @GetMapping("/register")
    public String registerPage(Model model){
        RegistrationDto user = new RegistrationDto();
        model.addAttribute("user", user);

        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user")RegistrationDto user,
                           BindingResult result, Model model, @RequestParam("pozaProfil") MultipartFile file){
        if(result.hasErrors()){
            System.out.println(result.getAllErrors());

            model.addAttribute("user", user);
            return "register";
        }

        UserEntity existingUserEmail = userService.findByEmail(user.getEmail());
        if(existingUserEmail != null && existingUserEmail.getEmail() != null && !existingUserEmail.getEmail().isEmpty()){
            result.rejectValue("email", "error.email","Există deja un user cu această adresă de email.");
        }
        UserEntity existingUserUsername = userService.findByUsername(user.getUsername());
        if(existingUserUsername != null && existingUserUsername.getUsername() != null && !existingUserUsername.getUsername().isEmpty()){
            result.rejectValue("username","error.username", "Există deja un user cu acest nume de utilizator.");
        }
        if(!user.getParola().equals(user.getConfirmareParola())){
            result.rejectValue("confirmareParola", "error.confirmareParola","Parolele nu corespund.");
        }

        userService.saveUser(user);
        imageService.saveUserPhoto(user.getId(), file);

        return "redirect:/login?success";
    }

    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }
}
