package com.hike.controller;

import com.hike.models.UserEntity;
import com.hike.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.UnsupportedEncodingException;
import java.net.http.HttpRequest;

@Controller
public class ContactController {

    private MailService mailService;

    @Autowired
    public ContactController(MailService mailService) {
        this.mailService = mailService;
    }

    @GetMapping("/contact")
    public String contactPage(Model model)
    {
        return "contact";
    }

    @PostMapping("/contact")
    public String contactMessage(HttpServletRequest request, Model model)
    {
        String content = request.getParameter("mesaj");
        String nume = request.getParameter("nume");
        String email = request.getParameter("email");
        String subiect = request.getParameter("subiect");

        try{
            mailService.sendContactEmail(email, nume, subiect, content);
            model.addAttribute("status", "Mesajul a fost trimis!");
        }
        catch (MessagingException | UnsupportedEncodingException e){
            model.addAttribute("status", "Emailul nu a putut fi trimis. Încearcă din nou.");
        }

        return "contact";
    }
}
