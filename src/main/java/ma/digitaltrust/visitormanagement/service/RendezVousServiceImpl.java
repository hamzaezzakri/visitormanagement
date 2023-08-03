package ma.digitaltrust.visitormanagement.service;

import ma.digitaltrust.visitormanagement.config.RendezVousProps;
import ma.digitaltrust.visitormanagement.dao.RendezVousRepository;
import ma.digitaltrust.visitormanagement.model.Etat;
import ma.digitaltrust.visitormanagement.model.RendezVous;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@EnableConfigurationProperties(value = RendezVousProps.class)
public class RendezVousServiceImpl implements IRendezVousService {

    @Autowired
    private RendezVousRepository rendezVousRepository;
    @Autowired
    private RendezVousProps rendezVousProps;

    @Override
    public List<RendezVous> getAll() {

        return rendezVousRepository.findAll();
    }

    @Override
    public void addRendezVous(RendezVous rendezVous) {

        rendezVous.setEtat(Etat.EN_ATTENTE);
        rendezVousRepository.save(rendezVous);
    }

    @Override
    public void modifierRendezVous(RendezVous rendezVous) {

        rendezVousRepository.save(rendezVous);
    }

    @Override
    public boolean existsByDateVisiteAndHeureVisite(LocalDate dateVisite, LocalTime heureVisite) {

        return rendezVousRepository.existsByDateVisiteAndHeureVisite(dateVisite, heureVisite);
    }

    @Override
    public RendezVous findByDateVisiteAndHeureVisite(LocalDate dateVisite, LocalTime heureVisite) {

        return rendezVousRepository.findByDateVisiteAndHeureVisite(dateVisite, heureVisite);
    }

    @Override
    public List<RendezVous> findByIdCard(String idCard) {

        return rendezVousRepository.findByIdCard(idCard);
    }

    @Override
    public RendezVous findById(Long id) {

        return rendezVousRepository.findById(id).get();
    }

    @Override
    public List<String> getHeuresProposees(String dateChoisie) {

        List<String> heuresProposees = rendezVousProps.getHeuresProposees();

        List<String> heuresOccupees = rendezVousRepository.findAll().stream()
                .filter(rdv -> rdv.getDateVisite().compareTo(LocalDate.parse(dateChoisie)) == 0)
                .map(rdv -> rdv.getHeureVisite().format(DateTimeFormatter.ofPattern("HH:mm")))
                .collect(Collectors.toList());

        return heuresProposees.stream()
                .filter(i -> !heuresOccupees.contains(i))
                .collect(Collectors.toList());
    }

    @Override
    public List<RendezVous> getAllRdvsAfterNow(){

        return rendezVousRepository.findAll().stream()
                .filter(rdv -> rdv.getDateVisite().compareTo(LocalDate.now()) > 0
                        || (rdv.getDateVisite().compareTo(LocalDate.now()) == 0
                        && rdv.getHeureVisite().compareTo(LocalTime.now()) > 0))
                .collect(Collectors.toList());
    }
}
