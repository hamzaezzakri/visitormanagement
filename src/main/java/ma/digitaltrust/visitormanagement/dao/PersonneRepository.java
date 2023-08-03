package ma.digitaltrust.visitormanagement.dao;

import ma.digitaltrust.visitormanagement.model.Personne;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonneRepository extends JpaRepository<Personne, Long> {
}
