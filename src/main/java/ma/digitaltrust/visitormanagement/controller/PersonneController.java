package ma.digitaltrust.visitormanagement.controller;

import ma.digitaltrust.visitormanagement.model.Personne;
import ma.digitaltrust.visitormanagement.service.IPersonneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/personne")
public class PersonneController {

    @Autowired
    private IPersonneService personneService;

    @GetMapping("/all")
    public ResponseEntity<List<Personne>> getAll(){

        return ResponseEntity.ok(personneService.getAll());
    }

    @GetMapping("/load")
    public ResponseEntity<List<String>> load(){

        return ResponseEntity.ok(personneService.getAll().stream()
                .map(p -> p.getPrenom() + " " + p.getNom())
                .collect(Collectors.toList()));
    }


}
