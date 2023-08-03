package ma.digitaltrust.visitormanagement.service;

import ma.digitaltrust.visitormanagement.model.RendezVous;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface IRendezVousService {

    List<RendezVous> getAll();
    void addRendezVous(RendezVous rendezVous);
    void modifierRendezVous(RendezVous rendezVous);
    boolean existsByDateVisiteAndHeureVisite(LocalDate dateVisite, LocalTime heureVisite);
    RendezVous findByDateVisiteAndHeureVisite(LocalDate dateVisite, LocalTime heureVisite);
    List<RendezVous> findByIdCard(String idCard);
    RendezVous findById(Long id);
    List<String> getHeuresProposees(String dateChoisie);
    public List<RendezVous> getAllRdvsAfterNow();
}
