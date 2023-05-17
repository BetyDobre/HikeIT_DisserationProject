package com.hike.controller;

import com.hike.dto.SalvamontDto;
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
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
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
    private SalvamontService salvamontService;
    private StireService stireService;
    private final int pageSize = 6;

    @Autowired
    public TraseuController(TraseuService traseuService, GrupaMuntoasaService grupaMuntoasaService, UserService userService,
                            MarcajService marcajService, TraseuCommentService traseuCommentService, MailService mailService,
                            WeatherService weatherService, SalvamontService salvamontService, StireService stireService) {
        this.traseuService = traseuService;
        this.grupaMuntoasaService = grupaMuntoasaService;
        this.userService = userService;
        this.marcajService = marcajService;
        this.traseuCommentService = traseuCommentService;
        this.mailService = mailService;
        this.weatherService = weatherService;
        this.salvamontService = salvamontService;
        this.stireService = stireService;
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

        traseuDto.setDescriere(traseuDto.getDescriere().replaceAll("\n", "<br>"));

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

        Salvamont salvamont = salvamontService.findByGrupa(traseu.get().getGrupaMuntoasa());
        if(salvamont != null){
            model.addAttribute("salvamont", salvamont);
        }

        model.addAttribute("stiri", stireService.findStiriByTraseu(traseu.get()));

        return "traseuDetails";
    }

    @GetMapping("/{id}/sterge")
    public String stergeTraseuAdmin(Model model, @PathVariable("id") Long id, @RequestParam(value = "page", defaultValue = "1", required = false) int pageNo){
        Traseu traseu = traseuService.getTraseuById(id).get();
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
        traseuDto.setDescriere(traseuDto.getDescriere().replaceAll("<br>", "\n"));

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

        traseuDto.setDescriere(traseuDto.getDescriere().replaceAll("\n", "<br>"));

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
        Traseu traseu = traseuService.getTraseuById(traseuId).get();
        traseuCommentService.delete(commId);
        addCommonAttributesComments(model, pageNo, traseuId);

        return "redirect:/trasee/" + traseuId +"?stergeSucces";
    }

    @GetMapping("/actualizareStiri")
    public String actualizareStiri(Model model, @RequestParam(value = "page", defaultValue = "1", required = false) int pageNo){
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer token");
        headers.setBearerAuth("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6IlJUTkVOMEl5T1RWQk1UZEVRVEEzUlRZNE16UkJPVU00UVRRM016TXlSalUzUmpnMk4wSTBPQSJ9.eyJodHRwczovL3VpcGF0aC9lbWFpbCI6Im1paGFlbGEtYmVhdHJpY2UuZG9icmVAbXkuZm1pLnVuaWJ1Yy5ybyIsImh0dHBzOi8vdWlwYXRoL2VtYWlsX3ZlcmlmaWVkIjp0cnVlLCJpc3MiOiJodHRwczovL2FjY291bnQudWlwYXRoLmNvbS8iLCJzdWIiOiJnb29nbGUtb2F1dGgyfDEwOTAzODM3MzM5MDU4MDA5NzU2OSIsImF1ZCI6WyJodHRwczovL29yY2hlc3RyYXRvci5jbG91ZC51aXBhdGguY29tIiwiaHR0cHM6Ly91aXBhdGguZXUuYXV0aDAuY29tL3VzZXJpbmZvIl0sImlhdCI6MTY4MzkxNzc3OCwiZXhwIjoxNjg0MDA0MTc4LCJhenAiOiI4REV2MUFNTlhjelczeTRVMTVMTDNqWWY2MmpLOTNuNSIsInNjb3BlIjoib3BlbmlkIHByb2ZpbGUgZW1haWwgb2ZmbGluZV9hY2Nlc3MifQ.mleDz5SGmI7UmArk9XE-IArIem_oVCczfAmTWxGw5DVpqhQ1PUZ1Yur15UQEcSZ4ObVe5JybXMBW1MzIky7T3hKbsm13BVS8c4VT_tDxs4f4zirh8M8IXv5MSuFzgWV0GnAB-ab8_N-LQ0WOnTxtmiGQhjddmN8W9RyZkseALSyR4x28g0UNSTtPf2QET-9WwmNkXF3m_f_-8QyG8arciM8Zss939aTHnbq9xMj65gESXwl91-_wUlM5kCqxJamOjbywLTBsu65sUNPdgPKONiQJkFsDlhzaXQZQ8eV6cBdEiTj0XSn3SeW6faf8JE6iD4zyaTZUper8B9zRoTIM_g");
        headers.set("X-UIPATH-OrganizationUnitId", "4365602");

        String jsonBody = "{" +
                            "\"startInfo\": {" +
                                "\"ReleaseKey\": \"b804c2aa-a84d-40cc-9f84-8678a386a148\","+
                                " \"Strategy\": \"Specific\"," +
                                " \"RobotIds\": [1252894],"+
                                " \"NoOfRobots\": 0"+
                                "}" +
                            "}";

//        {
//            "startInfo": {
//            "ReleaseKey": "b804c2aa-a84d-40cc-9f84-8678a386a148",
//                    "Strategy": "Specific",
//                    "RobotIds":[1252894],
//            "NoOfRobots": 0
//        }
//        }

        HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);

        String url = "https://cloud.uipath.com/betydbr/DefaultTenant/orchestrator_/odata/Jobs/UiPath.Server.Configuration.OData.StartJobs";

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        model.addAttribute("paginaPrincipala", true);
        Page<Traseu> trasee = traseuService.getAllTraseeAprobate(PageRequest.of(pageNo-1, pageSize, Sort.by("updatedOn").descending()));
        model.addAttribute("trasee", trasee);
        model.addAttribute("grupeMuntoase", grupaMuntoasaService.findAllGroups());
        addCommonAttributesTrasee(model, pageNo);

        return "trasee";
    }
}
