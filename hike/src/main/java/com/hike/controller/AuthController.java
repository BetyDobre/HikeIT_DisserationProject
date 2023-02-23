package com.hike.controller;

import com.hike.dto.RegistrationDto;
import com.hike.models.UserEntity;
import com.hike.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import java.io.IOException;

@Controller
public class AuthController{
    private final UserService userService;

    public AuthController(UserService userService){
        this.userService = userService;
    }

    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
            throws ServletException {

        // Convert multipart object to byte[]
        binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
    }

    @GetMapping("/register")
    public String registerPage(Model model){
        RegistrationDto user = new RegistrationDto();
        model.addAttribute("user", user);

        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user")RegistrationDto user,
                           BindingResult result, Model model,
                           final @RequestParam("pozaProfil") MultipartFile file){
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

        try {
            byte[] byteObjects = file.getBytes();
            user.setPozaProfil(byteObjects);
        } catch (IOException e) {
            System.out.println("Couldn't set image for user: " + e.getMessage());
        }
        userService.saveUser(user);

        return "redirect:/login?success";
    }

    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }
}
