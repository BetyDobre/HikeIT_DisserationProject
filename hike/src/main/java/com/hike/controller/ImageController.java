package com.hike.controller;

import com.hike.models.*;
import com.hike.service.*;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Controller
public class ImageController {
    private final GrupaMuntoasaService grupaMuntoasaService;
    private final UserService userService;
    private final BlogPostService blogPostService;
    private final MarcajService marcajService;
    private final TraseuService traseuService;

    @Autowired
    public ImageController(GrupaMuntoasaService grupaMuntoasaService, UserService userService, BlogPostService blogPostService, MarcajService marcajService, TraseuService traseuService) {
        this.grupaMuntoasaService = grupaMuntoasaService;
        this.userService = userService;
        this.blogPostService = blogPostService;
        this.marcajService = marcajService;
        this.traseuService = traseuService;
    }

    @GetMapping("/grupaMuntoasa/getImage/{id}")
    public void downloadGrupaMuntImage(@PathVariable Long id, HttpServletResponse response) throws IOException {
        Optional<GrupaMuntoasa> grupaMuntoasa = grupaMuntoasaService.findGrupaMuntoasa(id);
        GrupaMuntoasa grupa = new GrupaMuntoasa();
        if(grupaMuntoasa.isPresent()){
            grupa = grupaMuntoasa.get();
        }
        if (grupa.getPozaHarta() != null) {
            response.setContentType("image/jpeg");
            InputStream is = new ByteArrayInputStream(grupa.getPozaHarta());
            try {
                IOUtils.copy(is, response.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping("/user/getProfilePhoto/{username}")
    public void downloadUserImage(@PathVariable String username, HttpServletResponse response) throws IOException {
        UserEntity user = userService.findByUsername(username);

        if (user.getPozaProfil() != null) {
            response.setContentType("image/jpeg");
            InputStream is = new ByteArrayInputStream(user.getPozaProfil());
            try {
                IOUtils.copy(is, response.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping("/blog/getBlogPhoto/{id}")
    public void downloadBlogImage(@PathVariable Long id, HttpServletResponse response) throws IOException {
        BlogPost blogPost = blogPostService.getById(id);

        if (blogPost.getPozaCoperta() != null) {
            response.setContentType("image/jpeg");
            InputStream is = new ByteArrayInputStream(blogPost.getPozaCoperta());
            try {
                IOUtils.copy(is, response.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping("/marcaje/getMarcajPhoto/{id}")
    public void downloadMarcajImage(@PathVariable Long id, HttpServletResponse response) throws IOException {
        Optional<Marcaj> marcaj = marcajService.findById(id);

        if (marcaj.get().getMarcaj() != null) {
            response.setContentType("image/jpeg");
            InputStream is = new ByteArrayInputStream(marcaj.get().getMarcaj());
            try {
                IOUtils.copy(is, response.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping("/trasee/getTraseuPhotos/{id}/{index}")
    public void downloadPozaTraseu(@PathVariable Long id, @PathVariable int index, HttpServletResponse response) throws IOException {
        Optional<Traseu> traseu = traseuService.getTraseuById(id);

        if (traseu.get().getPozeTraseu() != null) {
            response.setContentType("image/jpeg");
            InputStream is = new ByteArrayInputStream(traseu.get().getPozeTraseu().get(index));
            try {
                IOUtils.copy(is, response.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
