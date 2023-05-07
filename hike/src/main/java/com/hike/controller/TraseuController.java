package com.hike.controller;

import com.hike.dto.TraseuCommentDto;
import com.hike.dto.TraseuDto;
import com.hike.mapper.TraseuMapper;
import com.hike.models.*;
import com.hike.models.WeatherData;
import com.hike.service.WeatherService;
import com.hike.service.*;
import jakarta.mail.MessagingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/trasee")
public class TraseuController {
    private TraseuService traseuService;
    private GrupaMuntoasaService grupaMuntoasaService;
    private UserService userService;
    private TraseuCommentService traseuCommentService;
    private MarcajService marcajService;
    private MailService mailService;
    private WeatherService weatherService;
    private final int pageSize = 6;

    @Autowired
    public TraseuController(TraseuService traseuService, GrupaMuntoasaService grupaMuntoasaService, UserService userService, MarcajService marcajService, TraseuCommentService traseuCommentService, MailService mailService, WeatherService weatherService) {
        this.traseuService = traseuService;
        this.grupaMuntoasaService = grupaMuntoasaService;
        this.userService = userService;
        this.marcajService = marcajService;
        this.traseuCommentService = traseuCommentService;
        this.mailService = mailService;
        this.weatherService = weatherService;
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
    public String getTrasee(@RequestParam(value = "page", defaultValue = "1", required = false) int pageNo, Model model,
                            @RequestParam(name = "sezon", required = false) List<Sezon> sezoane,
                            @RequestParam(name = "dificultate", required = false) List<Dificultate> dificultati,
                            @RequestParam(name = "grupaMuntoasa", required = false) Long grupaMuntoasaId,
                            @RequestParam(name = "distanta", required = false, defaultValue = "0") Long distanta,
                            @RequestParam(name = "titlu", required = false) String titlu,
                            @RequestParam(name = "durata", required = false, defaultValue = "0") String durata){

        model.addAttribute("paginaPrincipala", true);

        model.addAttribute("grupaMuntoasaId", grupaMuntoasaId);
        model.addAttribute("sezonList", sezoane);
        model.addAttribute("dificultateList", dificultati);
        model.addAttribute("distanta", distanta);
        model.addAttribute("titlu", titlu);
        model.addAttribute("durata", durata);

        Specification<Traseu> spec = Specification.where(null);

        if (sezoane != null && !sezoane.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> root.get("sezon").in(sezoane));
        }

        if (dificultati != null && !dificultati.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> root.get("dificultate").in(dificultati));
        }

        if (grupaMuntoasaId != null) {
            Optional<GrupaMuntoasa> grupaMuntoasa = grupaMuntoasaService.findGrupaMuntoasa(grupaMuntoasaId);
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("grupaMuntoasa"), grupaMuntoasa.get()));
        }

        if (distanta != null && distanta != 0) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("distanta"), distanta));
        }

        if (titlu != null && !titlu.isBlank()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("titlu"), "%" + titlu + "%"));
        }

        if (durata != null && !durata.isBlank() && !durata.equals("0")) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("durataMaxima"), durata + ":%h"));
        }

        spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("aprobat"), true));
        Page<Traseu> trasee = traseuService.findAll(spec, PageRequest.of(pageNo-1, pageSize, Sort.by("updatedOn").descending()));

        model.addAttribute("trasee", trasee);
        model.addAttribute("grupeMuntoase", grupaMuntoasaService.findAllGroups());

        addCommonAttributesTrasee(model, pageNo);

        return "trasee";
    }

    @GetMapping("/propuneri")
    public String propuneriTrasee(Model model, @RequestParam(value = "page", defaultValue = "1", required = false) int pageNo,
                                  @RequestParam(name = "search", required = false) String titlu){
        Specification<Traseu> spec = Specification.where(null);
        if (titlu != null && !titlu.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("titlu"), "%" + titlu + "%"));
        }
        spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("aprobat"), false));

        Page<Traseu> trasee = traseuService.findAll(spec, PageRequest.of(pageNo-1, pageSize, Sort.by("updatedOn").descending()));
        model.addAttribute("titlu", titlu);
        model.addAttribute("trasee", trasee);
        addCommonAttributesTrasee(model, pageNo);

        return "trasee";
    }

    @GetMapping("/traseeAdaugate")
    public String traseeAdaugate(Model model, @RequestParam(value = "page", defaultValue = "1", required = false) int pageNo,
                                 @RequestParam(name = "search", required = false) String titlu){
        String username = Utility.getLoggedUser();
        UserEntity user = userService.findByUsername(username);

        Specification<Traseu> spec = Specification.where(null);
        if (titlu != null && !titlu.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("titlu"), "%" + titlu + "%"));
        }
        spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("user"), user));

        Page<Traseu> trasee = traseuService.findAll(spec, PageRequest.of(pageNo-1, pageSize, Sort.by("updatedOn").descending()));
//        Page<Traseu> trasee = traseuService.getAllByUser(user, PageRequest.of(pageNo-1, pageSize, Sort.by("createdOn").descending()));

        model.addAttribute("trasee", trasee);
        addCommonAttributesTrasee(model, pageNo);
        model.addAttribute("aprobare", true);
        model.addAttribute("titlu", titlu);

        return "trasee";
    }

    @GetMapping("/traseeParcurse")
    public String traseeParcurse(Model model, @RequestParam(value = "page", defaultValue = "1", required = false) int pageNo,
                                 @RequestParam(name = "search", required = false) String titlu){
        String username = Utility.getLoggedUser();
        UserEntity user = userService.findByUsername(username);

        Pageable pageable = PageRequest.of(pageNo-1, pageSize, Sort.by("updatedOn").descending());
        Page<Traseu> trasee = userService.getTraseeParcurseByUser(user, pageable);

        if(titlu != null && !titlu.isEmpty()) {
            List<Traseu> traseeFiltrate = trasee.stream()
                    .filter(t -> t.getTitlu().toLowerCase().contains(titlu.toLowerCase()))
                    .collect(Collectors.toList());
            trasee = new PageImpl<>(traseeFiltrate, pageable, traseeFiltrate.size());
        }
        model.addAttribute("trasee", trasee);
        model.addAttribute("titlu", titlu);

        addCommonAttributesTrasee(model, pageNo);

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
                               BindingResult result, Model model, @RequestParam(value = "pozeTraseu", required = false) List<MultipartFile> files) {
        Traseu traseuExisting = traseuService.getTraseuByTitlu(traseuDto.getTitlu());
        if (traseuExisting != null && traseuExisting.getTitlu() != null && !traseuExisting.getTitlu().isEmpty()) {
            result.rejectValue("titlu", "error.titlu", "Există deja un traseu cu aceast titlu.");
        }
        if (result.hasErrors()) {
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
        if (isAdmin) {
            traseuDto.setAprobat(true);
        } else {
            traseuDto.setAprobat(false);
        }

        if (!files.get(0).isEmpty()){
            try {
                List<byte[]> poze = new ArrayList<>();
                for (MultipartFile file : files) {
                    if (file.getOriginalFilename() != "") {
                        byte[] poza = file.getBytes();
                        poze.add(poza);
                    }
                }
                traseuDto.setPozeTraseu(poze);
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("error", "A apărut o eroare la încărcarea pozelor.");
            }
        }
        else{
            traseuDto.setPozeTraseu(null);
        }


        traseuService.save(traseuDto);

        return "redirect:/trasee?adaugaSuccess";
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

        String username = Utility.getLoggedUser();
        UserEntity user = userService.findByUsername(username);
        if(user != null){
            boolean traseuParcurs = user.getTraseeParcurse().stream().anyMatch(t -> Objects.equals(t.getId(), id));
            model.addAttribute("traseuParcurs", traseuParcurs);
        }

        model.addAttribute("comentariuNou", new TraseuCommentDto());
        addCommonAttributesComments(model, pageNo, id);

        List<String> coordonate = List.of(traseu.get().getPunctSosire().split(","));
        List<WeatherData> weatherData = weatherService.getWeatherData(Double.parseDouble(coordonate.get(0)), Double.parseDouble(coordonate.get(0)));
        model.addAttribute("weather", weatherData);

        return "traseuDetails";
    }

    @GetMapping("/{id}/sterge")
    public String stergeTraseuAdmin(Model model, @PathVariable("id") Long id, @RequestParam(value = "page", defaultValue = "1", required = false) int pageNo){
        traseuService.delete(id);
        addCommonAttributesTrasee(model, pageNo);

        return "redirect:/trasee?stergeSuccess";
    }

    @GetMapping("/{id}/aproba")
    public String aprobaTraseuAdmin(Model model, @PathVariable("id") Long id, @RequestParam(value = "page", defaultValue = "1", required = false) int pageNo){
        Optional<Traseu> traseuOpt = traseuService.getTraseuById(id);
        if(traseuOpt.isPresent()){
            traseuOpt.get().setAprobat(true);
            traseuService.save(traseuOpt.get());
        }
        else {
            model.addAttribute("error", "Traseul nu a fost gasit");
            return "404";
        }

        addCommonAttributesTrasee(model, pageNo);

        return "redirect:/trasee?adaugaSuccess";
    }

    @GetMapping("/{id}/respinge")
    public String respingeTraseuAdmin(Model model, @PathVariable("id") Long id, @RequestParam(value = "page", defaultValue = "1", required = false) int pageNo){
        Optional<Traseu> traseuOpt = traseuService.getTraseuById(id);
        if(traseuOpt.isPresent()){
            String email = traseuOpt.get().getUser().getEmail();
            String content = "<p>Salut, </p>"
                    + "<p>Te anunțăm că propunerea ta pentru traseul cu titlul "+ traseuOpt.get().getTitlu() +" a fost respinsă, iar cererea a fost ștearsă.</p>"
                    +"<br/>"
                    + "<p>Informațiile oferite nu sunt corecte sau sunt incomplete. Te rugăm să documentezi mai bine traseul propus și să furnizezi corect informațiile cerute în formular.</p>"
                    + "<p>Mulțumim!</p>";
            try {
                mailService.sendEmail("respingereTraseu", email, content);
            } catch (MessagingException | UnsupportedEncodingException e){
                    model.addAttribute("error", "Emailul nu a putut fi trimis. Încearcă din nou mai târziu.");
            }

            traseuService.respinge(traseuOpt.get());
        }
        else {
            model.addAttribute("error", "Traseul nu a fost gasit");
            return "404";
        }

        addCommonAttributesTrasee(model, pageNo);

        return "redirect:/trasee?stergeSuccess";
    }

    @GetMapping("/{id}/edit")
    public String editareTraseu(@PathVariable Long id, Model model){
        Traseu traseu = traseuService.getTraseuById(id).get();
        TraseuDto traseuDto = TraseuMapper.mapToTraseuDto(traseu);

        model.addAttribute("traseu", traseuDto);
        model.addAttribute("grupeMuntoase", grupaMuntoasaService.findAllGroups());
        model.addAttribute("marcaje", marcajService.getAllMarcaje());

        return "traseuEdit";
    }

    @PostMapping("/edit")
    public String updateTraseu(@Valid @ModelAttribute("traseu") TraseuDto traseuDto,
                               BindingResult result, Model model, @RequestParam("pozeTraseu") List<MultipartFile> files){
        Traseu traseuExisting = traseuService.getTraseuByTitlu(traseuDto.getTitlu());
        if(traseuExisting != null && traseuExisting.getTitlu() != null && !traseuExisting.getTitlu().isEmpty() && traseuExisting.getId() != traseuDto.getId()){
            result.rejectValue("titlu", "error.titlu","Există deja un traseu cu aceast titlu.");
        }
        if(result.hasErrors()){
            System.out.println(result.getAllErrors());

            model.addAttribute("traseu", traseuDto);
            model.addAttribute("grupeMuntoase", grupaMuntoasaService.findAllGroups());
            model.addAttribute("marcaje", marcajService.getAllMarcaje());
            return "traseuForm";
        }


        if (files.get(0).isEmpty()) {
            traseuDto.setPozeTraseu(traseuService.getTraseuById(traseuDto.getId()).get().getPozeTraseu());
        }
        else{
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
        }

        traseuService.save(traseuDto);

        return "redirect:/trasee?modificaSuccess";
    }

    @GetMapping("/{id}/parcurs")
    public String marcheazaCaRealizat(Model model, @PathVariable("id") Long id, @RequestParam(value = "page", defaultValue = "1", required = false) int pageNo){
        Optional<Traseu> traseuOpt = traseuService.getTraseuById(id);
        String username = Utility.getLoggedUser();
        UserEntity user = userService.findByUsername(username);
        if(traseuOpt.isPresent()){
            List<Traseu> traseeParcurse = user.getTraseeParcurse();
            boolean traseuParcursBool = traseeParcurse.stream().anyMatch(t -> Objects.equals(t.getId(), id));
            if(traseuParcursBool){
                traseeParcurse.remove(traseuOpt.get());
            }
            else{
                traseeParcurse.add(traseuOpt.get());
            }
            user.setTraseeParcurse(traseeParcurse);
            userService.save(user);
        }
        else {
            model.addAttribute("error", "Traseul nu a fost gasit");
            return "404";
        }

        addCommonAttributesTrasee(model, pageNo);

        return "redirect:/trasee/" + id+ "?marcatSuccess";
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
