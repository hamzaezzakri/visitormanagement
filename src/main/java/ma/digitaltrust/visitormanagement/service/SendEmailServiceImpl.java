package ma.digitaltrust.visitormanagement.service;

import com.google.zxing.WriterException;
import ma.digitaltrust.visitormanagement.model.RendezVous;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class SendEmailServiceImpl implements ISendEmailService {

    @Value("${api.url}")
    private String apiUrl;
    @Value("${app.email}")
    private String email;
    @Value("${app.personal}")
    private String personal;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private SpringTemplateEngine templateEngine;
    @Autowired
    private IRendezVousService rendezVousService;
    @Autowired
    private IQRCodeGenerator qrCodeGenerator;

    @Override
    public void sendConfirmationEmail(String to, RendezVous rendezVous) throws MessagingException, IOException, WriterException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,true);

        RendezVous rdv = rendezVousService.findByDateVisiteAndHeureVisite(rendezVous.getDateVisite(), rendezVous.getHeureVisite());

        Context context = new Context();
        Map<String, Object> properties = new HashMap<>();
        properties.put("rendezVous", rendezVous);
        properties.put("dateVisite", rendezVous.getDateVisite().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)));
        properties.put("heureVisite", rendezVous.getHeureVisite().format(DateTimeFormatter.ofPattern("HH:mm")));
        properties.put("idRDV", Base64.getEncoder().encodeToString(String.valueOf(rdv.getId()).getBytes()));
        properties.put("apiUrl", apiUrl);
        context.setVariables(properties);

        helper.setFrom(email, personal);
        helper.setTo(to);
        helper.setSubject("Confirmation de rendez-vous du " + rendezVous.getDateVisite().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)) + " à " + rendezVous.getHeureVisite().format(DateTimeFormatter.ofPattern("HH:mm")));

        String html = templateEngine.process("confirmation-email.html",context);
        helper.setText(html,true);

        mailSender.send(message);
    }

    @Override
    public void sendAcceptEmail(RendezVous rendezVous) throws MessagingException, IOException, WriterException {

        //String QR_CODE_IMAGE_PATH = "./src/main/resources/static/images/"+ rendezVous.getId() + ".png";

        //qrCodeGenerator.getQRCodeImage(rendezVous.toString(),250,250, QR_CODE_IMAGE_PATH);

        byte[] qrcode = qrCodeGenerator.getQRCodeByte(rendezVous.toString(),250,250);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

        Context context = new Context();
        Map<String, Object> properties = new HashMap<>();
        properties.put("prenom", rendezVous.getPrenom());
        properties.put("nom", rendezVous.getNom());
        properties.put("invitePar", rendezVous.getInvitePar());
        properties.put("date", rendezVous.getDateVisite().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)));
        properties.put("heure", rendezVous.getHeureVisite().format(DateTimeFormatter.ofPattern("HH:mm")));
        context.setVariables(properties);

        helper.setFrom(email, personal);
        helper.setTo(rendezVous.getEmail());
        helper.setSubject("Confirmation de rendez-vous avec " + rendezVous.getInvitePar());

        String html = templateEngine.process("accept-email.html",context);
        helper.setText(html,true);

        /*FileSystemResource fileSystemResource = new FileSystemResource(QR_CODE_IMAGE_PATH);
        helper.addInline("qrcode", fileSystemResource);*/

        InputStreamSource source = new ByteArrayResource(qrcode);
        helper.addInline("qrcode", source, "image/png");

        mailSender.send(message);

        //Files.deleteIfExists(Paths.get(QR_CODE_IMAGE_PATH));
    }

    @Override
    public void sendRefuseEmail(String to, String prenom, String nom, String invitePar, String date, String heure) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

        Context context = new Context();
        Map<String, Object> properties = new HashMap<>();
        properties.put("prenom", prenom);
        properties.put("nom", nom);
        properties.put("invitePar", invitePar);
        properties.put("date", date);
        properties.put("heure", heure);
        context.setVariables(properties);

        helper.setFrom(email, personal);
        helper.setTo(to);
        helper.setSubject("Annulation  de rendez-vous avec " + invitePar);

        String html = templateEngine.process("refuse-email.html",context);
        helper.setText(html,true);

        mailSender.send(message);
    }
}
