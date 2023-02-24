package com.hike.controller;

import com.hike.dto.GrupaMuntoasaDto;
import com.hike.exception.ObjectNotFoundException;
import com.hike.models.UserEntity;
import com.hike.repository.UserRepository;
import com.hike.service.GrupaMuntoasaService;
import com.hike.service.MailService;
import com.hike.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
public class MainController {
    private GrupaMuntoasaService grupaMuntoasaService;
    private MailService mailService;
    private UserService userService;
    private UserRepository userRepository;

    @Autowired
    public MainController(GrupaMuntoasaService grupaMuntoasaService, MailService mailService, UserService userService, UserRepository userRepository) {
        this.grupaMuntoasaService = grupaMuntoasaService;
        this.mailService = mailService;
        this.userService = userService;
        this.userRepository = userRepository;
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

    @GetMapping("/subscribe")
    public String subscribeToNewsletter(HttpServletRequest request, Model model){
        String content = "<p>Salut,</p>"
                + "<p>Mulțumim ca te-ai abonat la newsletter-ul nostru!</p>"
                + "<p>În acest fel vei afla primul noutățile despre site-ul nostru si despre traseele din România.</p>"
                + "<p>Pe curând!</p>";

        try{
            mailService.sendEmail("newsletter",request.getParameter("email"), content);
            UserEntity user = userService.findByEmail(request.getParameter("email"));
            if (user != null){
                user.setNewsletter(true);
                userRepository.save(user);
            }
            model.addAttribute("status", "Abonarea a fost efectuată cu succes!");
        }
        catch (MessagingException | UnsupportedEncodingException e){
            model.addAttribute("status", "Emailul nu a putut fi trimis. Încearcă din nou mai târziu.");
        }

        return "index";
    }
}
