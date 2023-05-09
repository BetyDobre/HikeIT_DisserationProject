package com.hike.controller;

import com.hike.dto.MarcajDto;
import com.hike.dto.SalvamontDto;
import com.hike.mapper.SalvamontMapper;
import com.hike.models.GrupaMuntoasa;
import com.hike.models.Marcaj;
import com.hike.models.Salvamont;
import com.hike.service.GrupaMuntoasaService;
import com.hike.service.SalvamontService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/salvamont")
public class SalvamontController {
    private SalvamontService salvamontService;
    private GrupaMuntoasaService grupaMuntoasaService;

    @Autowired
    public SalvamontController(SalvamontService salvamontService, GrupaMuntoasaService grupaMuntoasaService) {
        this.salvamontService = salvamontService;
        this.grupaMuntoasaService = grupaMuntoasaService;
    }

    @GetMapping
    public String getSalvamonti(Model model){
        model.addAttribute("salvamonti", salvamontService.getAll());
        return "salvamont";
    };

    @GetMapping("/adauga")
    public String adaugaSalvamont(Model model){
        model.addAttribute("salvamont", new SalvamontDto());
        model.addAttribute("grupeMuntoase", grupaMuntoasaService.findAllGroups());

        return "salvamontForm";
    }

    @PostMapping("/adauga")
    public String adaugaSalvamont(@Valid @ModelAttribute("salvamont") SalvamontDto salvamontDto,
                               BindingResult result, Model model){

        GrupaMuntoasa grupaMuntoasa = grupaMuntoasaService.findGrupaMuntoasa(salvamontDto.getGrupaMuntoasaId()).get();
        Salvamont existingSalvamont = salvamontService.findByGrupa(grupaMuntoasa);
        if(existingSalvamont!= null && existingSalvamont.getId() != salvamontDto.getId()){
            result.rejectValue("salvamont", "error.grupaMuntoasa","Există deja un salvamont pe această grupă!");
        }
        salvamontDto.setGrupaMuntoasa(grupaMuntoasa);

        if(result.hasErrors()){
            model.addAttribute("salvamont", salvamontDto);
            return "salvamontForm";
        }

        salvamontService.save(salvamontDto);

        return "redirect:/salvamont?success";
    }

    @GetMapping("/{id}/sterge")
    public String stergeSalvamont(@PathVariable Long id, Model model){
        salvamontService.delete(id);

        return "redirect:/salvamont?success";
    }

    @GetMapping("/{id}/edit")
    public String editSalvamont(@PathVariable Long id, Model model){
        Salvamont salvamont = salvamontService.findById(id);
        SalvamontDto salvamontDto = SalvamontMapper.mapToSalvamontDto(salvamont);
        model.addAttribute("salvamont", salvamontDto);
        model.addAttribute("grupeMuntoase", grupaMuntoasaService.findAllGroups());

        return "salvamontForm";
    }
}
