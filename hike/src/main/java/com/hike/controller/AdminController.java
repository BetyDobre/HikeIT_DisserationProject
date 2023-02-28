package com.hike.controller;

import com.hike.models.Role;
import com.hike.models.UserEntity;
import com.hike.repository.RoleRepository;
import com.hike.repository.UserRepository;
import com.hike.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private UserService userService;
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Autowired
    public AdminController(UserService userService, RoleRepository roleRepository, UserRepository userRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public String getUseri(Model model){
        List<UserEntity> users = userService.getAllUsers();
        model.addAttribute("users", users);
        Role blogger = roleRepository.findByName("BLOGGER");
        model.addAttribute("bloggerRole", blogger);
        Role admin = roleRepository.findByName("ADMIN");
        model.addAttribute("adminRole", admin);

        return "users";
    }

    @GetMapping("/user/{id}/sterge")
    public String stergeUser(@PathVariable Long id, Model model){
        userService.delete(id);
        List<UserEntity> users = userService.getAllUsers();
        model.addAttribute("users", users);

        return "redirect:/admin/users";
    }

    @GetMapping("/user/{id}/toggleBlogger")
    public String toggleBloggerRole(@PathVariable Long id, Model model){
        Optional<UserEntity> userOp = userService.findById(id);
        if(userOp.isPresent()){
            UserEntity user = userOp.get();
            Role blogger = roleRepository.findByName("BLOGGER");
            if(user.getRoles().contains(blogger)){
                List<Role> roles = user.getRoles();
                roles.remove(blogger);
                user.setRoles(roles);
                userRepository.save(user);
            }
            else {
                List<Role> roles = user.getRoles();
                roles.add(blogger);
                user.setRoles(roles);
                userRepository.save(user);
            }
        }
        else {
            model.addAttribute("error", "Utilizatorul nu a fost gasit!");
            return "404";
        }

        List<UserEntity> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "redirect:/admin/users";
    }

    @GetMapping("/user/{id}/toggleAdmin")
    public String toggleAdminRole(@PathVariable Long id, Model model){
        Optional<UserEntity> userOp = userService.findById(id);
        if(userOp.isPresent()){
            UserEntity user = userOp.get();
            Role admin = roleRepository.findByName("ADMIN");
            if(user.getRoles().contains(admin)){
                List<Role> roles = user.getRoles();
                roles.remove(admin);
                user.setRoles(roles);
                userRepository.save(user);
            }
            else {
                List<Role> roles = user.getRoles();
                roles.add(admin);
                user.setRoles(roles);
                userRepository.save(user);
            }
        }
        else {
            model.addAttribute("error", "Utilizatorul nu a fost gasit!");
            return "404";
        }

        List<UserEntity> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "redirect:/admin/users";
    }
}
