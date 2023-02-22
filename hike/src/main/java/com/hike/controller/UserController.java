package com.hike.controller;

import com.hike.exception.ObjectNotFoundException;
import com.hike.models.UserEntity;
import com.hike.models.Utility;
import com.hike.service.MailService;
import com.hike.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.UnsupportedEncodingException;

@Controller
public class UserController {
    private UserService userService;
    private MailService mailService;

    @Autowired
    public UserController(UserService userService, MailService mailService) {
        this.userService = userService;
        this.mailService = mailService;
    }

    @GetMapping("/profil")
    public String getProfil(Model model){
        return "profil";
    }

    @GetMapping("/parolaUitata")
    public String parolaUitata(Model model){

        return "parolaUitata";
    }

    @PostMapping("/parolaUitata")
    public String procesareParolaUitata(HttpServletRequest request, Model model){
        String email = request.getParameter("email");
        String token = RandomString.make(45);

        try{
            userService.schimbaParolaToken(token, email);

            String resetParolaLink = Utility.getSiteURL(request) + "/resetParola?token=" + token;
            String content = "<p>Salut, </p>"
                    + "<p>Ai cerut resetarea parolei pentru contul tău.</p>"
                    + "<p>Apasă pe link-ul de mai jos pentru a-ți schimba parola: </p>"
                    + "<p><b><a href=\"" + resetParolaLink + "\">"+resetParolaLink+"</a><b></p>"
                    + "<p>Ignoră acest email dacă nu ai solicitat tu schimbarea parolei.</p>";

            mailService.sendEmail("reset",email, content);
            model.addAttribute("message", "Email-ul a fost trimis!");

        }
        catch (ObjectNotFoundException e){
            model.addAttribute("error", e.getMessage());
        }
        catch (MessagingException |  UnsupportedEncodingException e){
            model.addAttribute("error", "Emailul nu a putut fi trimis. Încearcă din nou mai târziu.");
        }

        return "parolaUitata";
    }

    @GetMapping("/resetParola")
    public String resetParola(@Param(value = "token") String token, Model model){
        UserEntity user = userService.findByToken(token);
        if(user == null){
            model.addAttribute("error", "Token invalid!");
            return "404";
        }

        model.addAttribute("token", token);
        return "resetParola";
    }

    @PostMapping("resetParola")
    public String procesareResetareParola(HttpServletRequest request, Model model){
        String token = request.getParameter("token");
        String parola = request.getParameter("parola");

        UserEntity user = userService.findByToken(token);
        if(user == null){
            model.addAttribute("error", "Token invalid!");
            return "404";
        }else{
            userService.schimbaParola(user, parola);
            model.addAttribute("message", "Parola a fost actualizată cu succes!");
        }

        return "login";
    }
}
