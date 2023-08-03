package ma.digitaltrust.visitormanagement.service;

import com.regula.documentreader.webclient.api.DocumentReaderApi;
import com.regula.documentreader.webclient.model.*;
import com.regula.documentreader.webclient.model.ext.ProcessRequestImage;
import com.regula.documentreader.webclient.model.ext.RecognitionRequest;
import com.regula.documentreader.webclient.model.ext.RecognitionResponse;
import ma.digitaltrust.visitormanagement.exception.BusinessException;
import ma.digitaltrust.visitormanagement.model.ProcessResponse;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;

import static com.regula.documentreader.webclient.model.TextFieldType.SURNAME;

@Service
public class ProcessServiceImpl implements IProcessService {

    @Override
    public ProcessResponse process(String imageBase64) throws IOException, MessagingException {

        ProcessResponse processResponse = null;
        int index = imageBase64.indexOf(",");

        imageBase64 = imageBase64.substring(index + 1);
        byte[] imageBytes = Base64.getDecoder().decode(imageBase64);
        ProcessRequestImage image = new ProcessRequestImage(imageBytes, Light.WHITE);

        ProcessParams processParams = new ProcessParams();
        processParams.setScenario(Scenario.FULL_PROCESS);
        processParams.setResultTypeOutput(Arrays.asList(Result.TEXT));
        processParams.setFieldTypesFilter(Arrays.asList(SURNAME, TextFieldType.GIVEN_NAME, TextFieldType.DOCUMENT_NUMBER, TextFieldType.PERSONAL_NUMBER));

        RecognitionRequest request = new RecognitionRequest(processParams, Arrays.asList(image));

        DocumentReaderApi api = new DocumentReaderApi();
        RecognitionResponse response = api.process(request);

        try{
            processResponse = new ProcessResponse();
            processResponse.setSurname(response.text().getField(SURNAME).getValue());
            processResponse.setGivenName(response.text().getField(TextFieldType.GIVEN_NAME).getValue());
            if(response.text().getField(TextFieldType.DOCUMENT_NUMBER) != null)
                processResponse.setDocumentNumber(response.text().getField(TextFieldType.DOCUMENT_NUMBER).getValue());
            if(response.text().getField(TextFieldType.PERSONAL_NUMBER) != null)
                processResponse.setPersonalNumber(response.text().getField(TextFieldType.PERSONAL_NUMBER).getValue());
        }
        catch(NullPointerException exception){
            throw new BusinessException("veuillez télécharger une carte d'identité valide");
        }
         return processResponse;
    }
}
