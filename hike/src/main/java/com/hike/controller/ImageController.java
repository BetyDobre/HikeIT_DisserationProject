package com.hike.controller;

import com.hike.models.GrupaMuntoasa;
import com.hike.service.GrupaMuntoasaService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Controller
public class ImageController {

    private final GrupaMuntoasaService grupaMuntoasaService;

    public ImageController(@Autowired GrupaMuntoasaService grupaMuntoasaService) {
        this.grupaMuntoasaService = grupaMuntoasaService;
    }

    @GetMapping("grupaMuntoasa/getImage/{id}")
    public void downloadImage(@PathVariable Long id, HttpServletResponse response) throws IOException {
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
}
