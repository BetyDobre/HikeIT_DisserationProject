package com.hike.controller;

import com.hike.dto.UserDto;
import com.hike.models.AuthProvider;
import com.hike.models.Role;
import com.hike.models.UserEntity;
import com.hike.models.Utility;
import com.hike.service.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import java.io.IOException;

import static com.hike.mapper.UserEditMapper.mapToUserDto;

@Controller
@RequestMapping("/user")
public class UserController {
    private UserService userService;
    private BlogPostService blogPostService;
    private BlogCommentService blogCommentService;
    private TraseuCommentService traseuCommentService;
    private TraseuService traseuService;

    @Autowired
    public UserController(UserService userService, BlogPostService blogPostService, BlogCommentService blogCommentService, TraseuCommentService traseuCommentService, TraseuService traseuService) {
        this.userService = userService;
        this.blogPostService = blogPostService;
        this.blogCommentService = blogCommentService;
        this.traseuCommentService = traseuCommentService;
        this.traseuService = traseuService;
    }

    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
            throws ServletException {

        // Convert multipart object to byte[]
        binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
    }

    @GetMapping("/profil")
    public String getProfil(Model model){
        String loggedUserUsername= Utility.getLoggedUser();
        UserEntity user = userService.findByUsername(loggedUserUsername);
        if(user == null){
            model.addAttribute("error", "Niciun user logat!");
            return "404";
        }

        UserDto userDto = mapToUserDto(user);
        model.addAttribute("user", userDto);
        model.addAttribute("informatii", "true");
        return "profil";
    }

    @PostMapping("/profil")
    public String updateUser(@Valid @ModelAttribute("user") UserDto userDto,
                             BindingResult result, Model model, @RequestParam("pozaProfil") MultipartFile file) {
        if(result.hasErrors()) {
            model.addAttribute("informatii", "true");
            model.addAttribute("user", userDto);
            return "profil";
        }

        UserEntity existingUserEmail = userService.findByEmail(userDto.getEmail());
        if(existingUserEmail != null && existingUserEmail.getEmail() != null && !existingUserEmail.getEmail().isEmpty()){
            result.rejectValue("email", "error.email","Există deja un user cu această adresă de email.");
        }
        UserEntity existingUserUsername = userService.findByUsername(userDto.getUsername());
        if(existingUserUsername != null && existingUserUsername.getUsername() != null && !existingUserUsername.getUsername().isEmpty()){
            result.rejectValue("username","error.username", "Există deja un user cu acest nume de utilizator.");
        }

        boolean needsLogout = false;
        UserEntity existingUser = userService.findById(userDto.getId()).get();
        if(!userDto.getUsername().equals(existingUser.getUsername())){
            needsLogout = true;
        }

        if (file.isEmpty() && userDto.getAuthProvider().equals(AuthProvider.LOCAL)) {
            userDto.setPozaProfil(existingUser.getPozaProfil());
        }
        else {
            try {
                byte[] byteObjects = file.getBytes();
                userDto.setPozaProfil(byteObjects);
            } catch (IOException e) {
                System.out.println("Couldn't set image for user: " + e.getMessage());
                model.addAttribute("error", "Imaginea aleasă nu poate fi încărcată. Încercați altă imagine.");
                model.addAttribute("informatii", "true");
                return "profil";
            }
        }

        userService.updateUser(userDto);

        if(needsLogout){
            return "redirect:/login?changeUsername";
        }

        return "redirect:/user/profil?success";
    }

    @GetMapping("/securitate")
    public String getProfilSecuritate(Model model){
        String loggedUserUsername= Utility.getLoggedUser();
        UserEntity user = userService.findByUsername(loggedUserUsername);

        if(user == null){
            model.addAttribute("error", "Niciun user logat!");
            return "404";
        }

        model.addAttribute("user", user);
        model.addAttribute("securitate", "true");
        return "profil";
    }

    @PostMapping("/schimbaParola")
    public String schimbaParola(Model model, HttpServletRequest request, HttpServletResponse response) {
        String loggedUserUsername= Utility.getLoggedUser();
        UserEntity user = userService.findByUsername(loggedUserUsername);
        if (user == null) {
            model.addAttribute("error", "Niciun user logat!");
            return "404";
        }

        BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
        boolean passChecker = bc.matches(request.getParameter("parola"), user.getParola());
        if (passChecker) {
            if(request.getParameter("parola").equals(request.getParameter("parolaNoua"))){
                model.addAttribute("error", "Noua parolă nu poate corespunde cu cea actuală.");
                model.addAttribute("securitate", "true");
                return "profil";
            }
            userService.schimbaParola(user, request.getParameter("parolaNoua"));
        }
        else {
            model.addAttribute("error", "Parola actuală nu este corectă.");
            model.addAttribute("securitate", "true");
            return "profil";
        }

        Utility.logout(request, response);
        model.addAttribute("message", "Parola a fost actualizată cu succes!");

        return "redirect:/login?changePass";
    }

    @GetMapping("/sterge")
    public String deleteUser(Model model) {
        String loggedUserUsername= Utility.getLoggedUser();
        UserEntity user = userService.findByUsername(loggedUserUsername);
        if(user == null){
            model.addAttribute("error", "Niciun user logat!");
            return "404";
        }

        userService.delete(user.getId());

        return "redirect:/logout";
    }

    @GetMapping("/statistici")
    public String getProfilStatistici(Model model){
        String loggedUserUsername= Utility.getLoggedUser();
        UserEntity user = userService.findByUsername(loggedUserUsername);
        if(user == null){
            model.addAttribute("error", "Niciun user logat!");
            return "404";
        }

        model.addAttribute("access", user.getRoles().stream().anyMatch(role -> role.getName().equals("BLOGGER")));
        model.addAttribute("user", user);
        model.addAttribute("statistici", "true");
        model.addAttribute("postCount", blogPostService.countAllByUser(user));
        model.addAttribute("commentCount", blogCommentService.countAllByUser(user) + traseuCommentService.countAllByUser(user));
        model.addAttribute("traseeAdaugateCount", traseuService.countTraseeAprobateByUser(user));
        model.addAttribute("traseeParcurseCount", userService.countTraseeParcurseByUser(user));

        return "profil";
    }

    @GetMapping("/termeni")
    public String getProfilTermeni(Model model){
        String loggedUserUsername= Utility.getLoggedUser();
        UserEntity user = userService.findByUsername(loggedUserUsername);
        if(user == null){
            model.addAttribute("error", "Niciun user logat!");
            return "404";
        }

        model.addAttribute("user", user);
        model.addAttribute("termeni", "true");

        return "profil";
    }
}
