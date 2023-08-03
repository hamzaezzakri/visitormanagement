package ma.digitaltrust.visitormanagement.service;

import ma.digitaltrust.visitormanagement.model.ProcessResponse;

import javax.mail.MessagingException;
import java.io.IOException;

public interface IProcessService {

    ProcessResponse process(String imageBase64) throws IOException, MessagingException;
}
