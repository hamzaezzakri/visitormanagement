package ma.digitaltrust.visitormanagement.service;

import com.google.zxing.WriterException;
import ma.digitaltrust.visitormanagement.model.RendezVous;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public interface ISendEmailService {

    void sendConfirmationEmail(String to, RendezVous rendezVous) throws MessagingException, IOException, WriterException;
    void sendAcceptEmail(RendezVous rendezVous) throws MessagingException, IOException, WriterException;
    void sendRefuseEmail(String to, String prenom, String nom, String invitePar, String date, String heure) throws MessagingException, UnsupportedEncodingException;
}
