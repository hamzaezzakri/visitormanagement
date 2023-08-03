package ma.digitaltrust.visitormanagement.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "rendez-vous")
public class RendezVousProps {

    private List<String> heuresProposees;
}
