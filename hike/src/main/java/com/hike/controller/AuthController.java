package com.hike.controller;

import com.hike.dto.RegistrationDto;
import com.hike.exception.ObjectNotFoundException;
import com.hike.models.AuthProvider;
import com.hike.models.UserEntity;
import com.hike.models.Utility;
import com.hike.service.MailService;
import com.hike.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import net.bytebuddy.utility.RandomString;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Controller
public class AuthController{
    private final UserService userService;
    private MailService mailService;

    public AuthController(UserService userService, MailService mailService){
        this.userService = userService;
        this.mailService = mailService;
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


    @GetMapping("/parolaUitata")
    public String parolaUitata(Model model){

        return "parolaUitata";
    }

    @PostMapping("/parolaUitata")
    public String procesareParolaUitata(HttpServletRequest request, Model model){
        String email = request.getParameter("email");
        String token = RandomString.make(45);

        UserEntity user = userService.findByEmail(email);
        if(user != null){
            if (user.getAuthProvider().equals(AuthProvider.GOOGLE)){
                model.addAttribute("error", "Acest email aparține unui cont Google. Parola nu poate fi schimbată.");
            }
            else {
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
                catch (MessagingException | UnsupportedEncodingException e){
                    model.addAttribute("error", "Emailul nu a putut fi trimis. Încearcă din nou mai târziu.");
                }
            }
        }
        else{
            model.addAttribute("error", "Acest email nu este asociat unui cont.");
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

    @PostMapping("/resetParola")
    public String procesareResetareParola(HttpServletRequest request, Model model){
        String token = request.getParameter("token");
        String parola = request.getParameter("parola");

        UserEntity user = userService.findByToken(token);
        if(user == null){
            model.addAttribute("error", "Token invalid!");
            return "404";
        }
        BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
        boolean passChecker = bc.matches(parola, user.getParola());
        if(passChecker){
            return "redirect:/resetParola?token=" + token + "&error";
        }
        else{
            userService.schimbaParola(user, parola);
            model.addAttribute("message", "Parola a fost actualizată cu succes!");
        }

        return "login";
    }
}
