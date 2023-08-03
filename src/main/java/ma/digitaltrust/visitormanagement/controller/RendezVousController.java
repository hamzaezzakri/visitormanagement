package ma.digitaltrust.visitormanagement.controller;

import com.google.zxing.WriterException;
import ma.digitaltrust.visitormanagement.model.Etat;
import ma.digitaltrust.visitormanagement.model.RendezVous;
import ma.digitaltrust.visitormanagement.model.SuccessResponse;
import ma.digitaltrust.visitormanagement.service.IPersonneService;
import ma.digitaltrust.visitormanagement.service.IRendezVousService;
import ma.digitaltrust.visitormanagement.service.ISendEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/rendez-vous")
public class RendezVousController {

    @Autowired
    private IRendezVousService rendezVousService;
    @Autowired
    private IPersonneService personneService;
    @Autowired
    private ISendEmailService emailService;
    @Autowired
    private SpringTemplateEngine templateEngine;

    @GetMapping("/all")
    public ResponseEntity<List<RendezVous>> getAll(){

        return ResponseEntity.ok(rendezVousService.getAll());
    }

    @GetMapping("/allRdvsAfterNow")
    public ResponseEntity<List<RendezVous>> getAllRdvsAfterNow(){

        return ResponseEntity.ok(rendezVousService.getAllRdvsAfterNow());
    }

    @GetMapping("/heures-proposees")
    public ResponseEntity<?> getHeuresProposees(@RequestParam String dateChoisie){

        if(LocalDate.parse(dateChoisie).compareTo(LocalDate.now()) <= 0)
            return ResponseEntity.badRequest().body(new SuccessResponse("veuillez choisir une date supérieure", HttpStatus.BAD_REQUEST.getReasonPhrase()));

        return ResponseEntity.ok(rendezVousService.getHeuresProposees(dateChoisie));
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveRendezVous(@RequestBody @Valid RendezVous rendezVous) throws MessagingException, IOException, WriterException {

        if(rendezVousService.existsByDateVisiteAndHeureVisite(rendezVous.getDateVisite(),rendezVous.getHeureVisite()))
            return ResponseEntity.badRequest().body(new SuccessResponse("rendez vous existe déjà, veuillez choisir une autre date", HttpStatus.BAD_REQUEST.getReasonPhrase()));

        List<RendezVous> rdvs = rendezVousService.findByIdCard(rendezVous.getIdCard());

        if(rdvs.size() > 0){
            if(rdvs.get(rdvs.size() -1).getDateVisite().compareTo(rendezVous.getDateVisite()) == 0)
                return ResponseEntity.badRequest().body(new SuccessResponse("vous ne pouvez pas prendre plusieurs rendez-vous dans la même journée", HttpStatus.BAD_REQUEST.getReasonPhrase()));
        }

        String invitePar =  personneService.getAll().stream()
                .filter(pers -> pers.getId() == Long.parseLong(rendezVous.getInvitePar()))
                .map(pers -> pers.getPrenom() + " " + pers.getNom())
                .findFirst().orElseThrow(()-> new NullPointerException("invité par n'existe pas"));

        String email = personneService.getAll().stream()
                .filter(pers -> pers.getId() == Long.parseLong(rendezVous.getInvitePar()))
                .map(pers -> pers.getEmail())
                .findFirst().get();

        /*String prenom =  personneService.getAll().stream()
                .filter(pers -> pers.getId() == Long.parseLong(rendezVous.getInvitePar()))
                .map(pers -> pers.getPrenom())
                .findFirst().get();*/

        rendezVous.setInvitePar(invitePar);
        rendezVousService.addRendezVous(rendezVous);
        //rendezVous.setInvitePar(prenom);
        emailService.sendConfirmationEmail(email, rendezVous);
        return new ResponseEntity<>(new SuccessResponse("rendez-vous enregistré avec succés", HttpStatus.CREATED.getReasonPhrase()),HttpStatus.CREATED);
    }

    @GetMapping(value = "/accept/{idRDV}")
    public ResponseEntity<?> accept(@PathVariable String idRDV) throws MessagingException, IOException, WriterException {

        RendezVous rendezVous = rendezVousService.findById(Long.parseLong(new String(Base64.getDecoder().decode(idRDV))));

        String jour = rendezVous.getDateVisite().format(DateTimeFormatter.ofPattern("EEEE"));
        String heure = rendezVous.getHeureVisite().format(DateTimeFormatter.ofPattern("HH:mm"));

        Context context = new Context();
        Map<String, Object> properties = new HashMap<>();
        properties.put("dateVisite", rendezVous.getDateVisite().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)));
        properties.put("heureVisite", heure);
        properties.put("jourHeure", jour.substring(0,1).toUpperCase() + jour.substring(1) + " | " + heure);
        context.setVariables(properties);

        if(rendezVous.getEtat().equals(Etat.ACCEPTE))
            return ResponseEntity.ok(templateEngine.process("alreadyAccept.html",context));
        if(rendezVous.getEtat().equals(Etat.REFUSE))
            return ResponseEntity.ok(templateEngine.process("alreadyRefuse.html",context));

        rendezVous.setEtat(Etat.ACCEPTE);
        rendezVousService.modifierRendezVous(rendezVous);
        emailService.sendAcceptEmail(rendezVous);
        /*emailService.sendAcceptEmail(rendezVous.getEmail(), rendezVous.getPrenom(), rendezVous.getInvitePar(),
                rendezVous.getDateVisite().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)),
                rendezVous.getHeureVisite().format(DateTimeFormatter.ofPattern("HH:mm")));*/

        return ResponseEntity.ok(templateEngine.process("accept.html",context));
    }

    @GetMapping(value = "/refuse/{idRDV}")
    public ResponseEntity<?> refuse(@PathVariable String idRDV) throws MessagingException, UnsupportedEncodingException {

        RendezVous rendezVous = rendezVousService.findById(Long.parseLong(new String(Base64.getDecoder().decode(idRDV))));

        String jour = rendezVous.getDateVisite().format(DateTimeFormatter.ofPattern("EEEE"));
        String heure = rendezVous.getHeureVisite().format(DateTimeFormatter.ofPattern("HH:mm"));

        Context context = new Context();
        Map<String, Object> properties = new HashMap<>();
        properties.put("dateVisite", rendezVous.getDateVisite().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)));
        properties.put("heureVisite", heure);
        properties.put("jourHeure", jour.substring(0,1).toUpperCase() + jour.substring(1) + " | " + heure);
        context.setVariables(properties);

        if(rendezVous.getEtat().equals(Etat.ACCEPTE))
            return ResponseEntity.ok(templateEngine.process("alreadyAccept.html",context));
        if(rendezVous.getEtat().equals(Etat.REFUSE))
            return ResponseEntity.ok(templateEngine.process("alreadyRefuse.html",context));

        rendezVous.setEtat(Etat.REFUSE);
        rendezVousService.modifierRendezVous(rendezVous);
        emailService.sendRefuseEmail(rendezVous.getEmail(), rendezVous.getPrenom(), rendezVous.getNom(), rendezVous.getInvitePar(),
                rendezVous.getDateVisite().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)),
                rendezVous.getHeureVisite().format(DateTimeFormatter.ofPattern("HH:mm")));

        return ResponseEntity.ok(templateEngine.process("refuse.html",context));
    }
}
