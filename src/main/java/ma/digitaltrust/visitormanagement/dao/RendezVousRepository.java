package ma.digitaltrust.visitormanagement.dao;

import ma.digitaltrust.visitormanagement.model.RendezVous;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface RendezVousRepository extends JpaRepository<RendezVous, Long> {

    boolean existsByDateVisiteAndHeureVisite(LocalDate dateVisite, LocalTime heureVisite);
    RendezVous findByDateVisiteAndHeureVisite(LocalDate dateVisite, LocalTime heureVisite);
    List<RendezVous> findByIdCard(String idCard);
}
