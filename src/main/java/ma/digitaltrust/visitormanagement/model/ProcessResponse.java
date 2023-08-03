package ma.digitaltrust.visitormanagement.model;

import lombok.Data;

@Data
public class ProcessResponse {

    private String surname;
    private String givenName;
    private String documentNumber;
    private String personalNumber;
}
