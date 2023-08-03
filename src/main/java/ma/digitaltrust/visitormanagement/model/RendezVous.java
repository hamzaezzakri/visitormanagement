package ma.digitaltrust.visitormanagement.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "rendezVous")
public class RendezVous {

    @ToString.Exclude
    @Id
    @GeneratedValue
    private Long id;
    @NotNull(message = "Le nom ne peut pas être nul")
    private String nom;
    @NotNull(message = "Le prénom ne peut pas être nul")
    private String prenom;
    @NotNull(message = "L'identifiant de la carte ne peut pas être nul")
    private String idCard;
    @ToString.Exclude
    @Email(message = "L'adresse e-mail doit être valide")
    @NotNull(message = "L'adresse e-mail ne peut pas être nulle")
    private String email;
    @NotNull(message = "Le champ invité par ne peut pas être nul")
    private String invitePar;
    @Future(message = "La date de la visite doit être dans le futur")
    @NotNull(message = "La date de la visite ne peut pas être nulle")
    private LocalDate dateVisite;
    @NotNull(message = "L'heure de la visite ne peut pas être nulle")
    private LocalTime heureVisite;
    @ToString.Exclude
    private boolean droit;
    @ToString.Exclude
    private boolean presence;
    @ToString.Exclude
    @Enumerated(EnumType.STRING)
    private Etat etat;

}
