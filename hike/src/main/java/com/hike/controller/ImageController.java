package com.hike.controller;

import com.hike.models.BlogPost;
import com.hike.models.GrupaMuntoasa;
import com.hike.models.Marcaj;
import com.hike.models.UserEntity;
import com.hike.service.BlogPostService;
import com.hike.service.GrupaMuntoasaService;
import com.hike.service.MarcajService;
import com.hike.service.UserService;
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

    @Autowired
    public ImageController(GrupaMuntoasaService grupaMuntoasaService, UserService userService, BlogPostService blogPostService, MarcajService marcajService) {
        this.grupaMuntoasaService = grupaMuntoasaService;
        this.userService = userService;
        this.blogPostService = blogPostService;
        this.marcajService = marcajService;
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
}
