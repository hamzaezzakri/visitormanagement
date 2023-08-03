package ma.digitaltrust.visitormanagement.controller;

import ma.digitaltrust.visitormanagement.service.IProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.IOException;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProcessController {

    @Autowired
    private IProcessService processService;

    @PostMapping("/process")
    public ResponseEntity<?> process(@RequestParam String imageBase64) throws IOException, MessagingException {

        return ResponseEntity.ok().body(processService.process(imageBase64));
    }
}
