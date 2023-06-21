package com.hike.controller;

import com.hike.dto.GrupaMuntoasaDto;
import com.hike.dto.UserDto;
import com.hike.exception.ObjectNotFoundException;
import com.hike.models.GrupaMuntoasa;
import com.hike.models.UserEntity;
import com.hike.models.Utility;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.UnsupportedEncodingException;
import java.util.Comparator;
import java.util.List;

import static com.hike.mapper.UserEditMapper.mapToUserDto;

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
        model.addAttribute("grupe", grupe.stream().sorted(Comparator.comparing(GrupaMuntoasaDto::getTitlu)));

        return "index";
    }

    @GetMapping("/404")
    public String errorPage(Model model) { return "404";}

    @GetMapping("/subscribe")
    public String subscribeToNewsletter(HttpServletRequest request, Model model){
        try{
            UserEntity user = userService.findByEmail(request.getParameter("email"));
            if (user != null){
                String dezabonareLink = Utility.getSiteURL(request) + "/dezabonare/"+ user.getId();
                String content = "<p>Salut,</p>"
                        + "<p>Mulțumim ca te-ai abonat la newsletter-ul nostru!</p>"
                        + "<p>În acest fel vei afla primul noutățile despre site-ul nostru si despre traseele din România.</p>"
                        + "<p>Pe curând!</p><b>"
                        + "<p>Pentru dezabonare apasa aici: <a href=\"" + dezabonareLink + "\">"+dezabonareLink+"</a><b></p>";
                mailService.sendEmail("newsletter",request.getParameter("email"), content);
                user.setNewsletter(true);
                userRepository.save(user);
                model.addAttribute("status", "Abonarea a fost efectuată cu succes!");
            }
            else {
                String registerLink = Utility.getSiteURL(request) + "/register";
                String content = "<p>Salut,</p>"
                        + "<p>Mulțumim ca vrei sa te abonezi la newsletter-ul nostru!</p>"
                        + "<p>În acest fel vei afla primul noutățile despre site-ul nostru si despre traseele din România.</p>"
                        + "<p>Pentru asta, mai întâi trebuie să te înregistrezi cu o adresă de email. Poti face asta <b><a href=\"" + registerLink + "\">aici</a><b>.<p>"
                        + "<p>Pe curând!</p>";
                mailService.sendEmail("newsletter",request.getParameter("email"), content);
                model.addAttribute("status", "A fost trimis un mail cu informații!");
            }
        }
        catch (MessagingException | UnsupportedEncodingException e){
            model.addAttribute("status", "Emailul nu a putut fi trimis. Încearcă din nou mai târziu.");
        }

        return "index";
    }

    @GetMapping("/dezabonare/{id}")
    public String dezabonare(Model model, @PathVariable Long id){
        UserEntity user = userService.findById(id).orElse(null);
        if(user!= null) {
            user.setNewsletter(false);
            UserDto userDto = mapToUserDto(user);
            userService.updateUser(userDto);
            model.addAttribute("message", "Dezabonarea a fost efectuată cu succes!");
        }
        else {
            model.addAttribute("message", "Utilizatorul nu a fost găsit!");
        }

        return "login";
    }
}
