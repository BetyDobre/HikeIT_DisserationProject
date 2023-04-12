package com.hike.controller;

import com.hike.dto.TraseuCommentDto;
import com.hike.dto.TraseuDto;
import com.hike.models.*;
import com.hike.service.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/trasee")
public class TraseuController {
    private TraseuService traseuService;
    private GrupaMuntoasaService grupaMuntoasaService;
    private UserService userService;
    private TraseuCommentService traseuCommentService;
    private MarcajService marcajService;
    private final int pageSize = 6;

    @Autowired
    public TraseuController(TraseuService traseuService, GrupaMuntoasaService grupaMuntoasaService, UserService userService, MarcajService marcajService, TraseuCommentService traseuCommentService) {
        this.traseuService = traseuService;
        this.grupaMuntoasaService = grupaMuntoasaService;
        this.userService = userService;
        this.marcajService = marcajService;
        this.traseuCommentService = traseuCommentService;
    }

    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
            throws ServletException {

        // Convert multipart object to byte[]
        binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
    }

    void addCommonAttributesTrasee(Model model, int pageNo){
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("link", "/trasee");

        List<Traseu> traseeAll = traseuService.findAll();
        Map<Long, Integer> nrComentarii = new HashMap<>();
        for (Traseu traseu : traseeAll) {
            Long traseuId = traseu.getId();
            int totalComentarii = traseuCommentService.noOfCommentsByTraseu(traseuId);
            nrComentarii.put(traseuId, totalComentarii);
        }
        model.addAttribute("nrComentarii", nrComentarii);
    }
    @GetMapping("")
    public String getTrasee(@RequestParam(value = "page", defaultValue = "1", required = false) int pageNo, Model model){
        Page<Traseu> trasee = traseuService.getAllTraseeAprobate(PageRequest.of(pageNo-1, pageSize));
        model.addAttribute("trasee", trasee);
        addCommonAttributesTrasee(model, pageNo);

        return "trasee";
    }

    @GetMapping("/propuneri")
    public String propuneriTrasee(Model model, @RequestParam(value = "page", defaultValue = "1", required = false) int pageNo){
        Page<Traseu> trasee = traseuService.getAllTraseeNeaprobate(PageRequest.of(pageNo-1, pageSize));
        model.addAttribute("trasee", trasee);
        addCommonAttributesTrasee(model, pageNo);

        return "trasee";
    }

    @GetMapping("/traseeAdaugate")
    public String traseeAdaugate(Model model, @RequestParam(value = "page", defaultValue = "1", required = false) int pageNo){
        String username = Utility.getLoggedUser();
        UserEntity user = userService.findByUsername(username);

        Page<Traseu> trasee = traseuService.getAllByUser(user, PageRequest.of(pageNo-1, pageSize));
        model.addAttribute("trasee", trasee);

        addCommonAttributesTrasee(model, pageNo);
        model.addAttribute("aprobare", true);

        return "trasee";
    }

    @GetMapping("/adauga")
    public String adaugaTraseuNou(Model model) {
        TraseuDto traseuDto = new TraseuDto();
        model.addAttribute("traseu", traseuDto);
        model.addAttribute("grupeMuntoase", grupaMuntoasaService.findAllGroups());
        model.addAttribute("marcaje", marcajService.getAllMarcaje());

        return "traseuForm";
    }

    @PostMapping("/adauga")
    public String adaugaTraseu(@Valid @ModelAttribute("traseu") TraseuDto traseuDto,
                               BindingResult result, Model model, @RequestParam("pozeTraseu") List<MultipartFile> files){
        Traseu traseuExisting = traseuService.getTraseuByTitlu(traseuDto.getTitlu());
        if(traseuExisting != null && traseuExisting.getTitlu() != null && !traseuExisting.getTitlu().isEmpty()){
            result.rejectValue("titlu", "error.titlu","Există deja un traseu cu aceast titlu.");
        }
        if(result.hasErrors()){
            System.out.println(result.getAllErrors());

            model.addAttribute("traseu", traseuDto);
            model.addAttribute("grupeMuntoase", grupaMuntoasaService.findAllGroups());
            model.addAttribute("marcaje", marcajService.getAllMarcaje());
            return "traseuForm";
        }

        String username = Utility.getLoggedUser();
        UserEntity user = userService.findByUsername(username);
        traseuDto.setUser(user);

        boolean isAdmin = user.getRoles().stream().anyMatch(role -> role.getName().equals("ADMIN"));
        if(isAdmin){
            traseuDto.setAprobat(true);
        }
        else{
            traseuDto.setAprobat(false);
        }

        try {
            List<byte[]> poze = new ArrayList<>();
            for (MultipartFile file : files) {
                byte[] poza = file.getBytes();
                poze.add(poza);
            }
            traseuDto.setPozeTraseu(poze);
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "A apărut o eroare la încărcarea pozelor.");
        }

        traseuService.save(traseuDto);

        return "redirect:/trasee?adaugaSuccess";
    }

    public void addCommonAttributesComments(Model model, int pageNo, Long id){
        Traseu traseu = traseuService.getTraseuById(id).get();
        String username = Utility.getLoggedUser();
        UserEntity user = userService.findByUsername(username);
        if(user != null){
            model.addAttribute("userId", user.getId());
        }
        else{
            model.addAttribute("userId", null);
        }

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("comentarii", traseuCommentService.getAllCommentsByTraseu(traseu, PageRequest.of(pageNo - 1, pageSize)));
        model.addAttribute("link", "/trasee/" + id);
    }

    @GetMapping("/{id}")
    public String getTraseuSingle(Model model, @PathVariable("id") Long id, @RequestParam(value = "page", defaultValue = "1", required = false) int pageNo){
        Optional<Traseu> traseu = traseuService.getTraseuById(id);
        if (traseu.isPresent()){
            model.addAttribute("traseu", traseu.get());
        }
        else {
            model.addAttribute("error", "Traseul căutat nu există.");
            return "404";
        }

        model.addAttribute("comentariuNou", new TraseuCommentDto());
        addCommonAttributesComments(model, pageNo, id);

        return "traseuDetails";
    }

    @PostMapping("/{id}")
    public String adaugaComentariu(Model model, @PathVariable("id") Long id,
                                   @ModelAttribute("comentariuNou") TraseuCommentDto traseuCommentDto,
                                   BindingResult result,
                                   @RequestParam(value = "page", defaultValue = "1", required = false) int pageNo){

        if(result.hasErrors()){
            System.out.println(result.getAllErrors());
            model.addAttribute("comentariuNou", traseuCommentDto);
            addCommonAttributesComments(model, pageNo, id);

            return "blogForm";
        }

        String username = Utility.getLoggedUser();
        UserEntity user = userService.findByUsername(username);
        traseuCommentDto.setTraseu(traseuService.getTraseuById(id).get());
        traseuCommentDto.setUser(user);

        addCommonAttributesComments(model, pageNo, id);
        traseuCommentService.save(traseuCommentDto);

        return "redirect:/trasee/" + id +"?adaugaSucces";
    }

    @GetMapping("/{traseuId}/{commId}/sterge")
    public String stergeComentariu(Model model, @PathVariable("traseuId") Long traseuId, @PathVariable("commId") Long commId,
                                   @RequestParam(value = "page", defaultValue = "1", required = false) int pageNo){
        traseuCommentService.delete(commId);
        addCommonAttributesComments(model, pageNo, traseuId);

        return "redirect:/trasee/" + traseuId +"?stergeSucces";
    }

}
