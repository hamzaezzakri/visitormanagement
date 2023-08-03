package ma.digitaltrust.visitormanagement;

import ma.digitaltrust.visitormanagement.dao.PersonneRepository;
import ma.digitaltrust.visitormanagement.dao.RendezVousRepository;
import ma.digitaltrust.visitormanagement.model.Etat;
import ma.digitaltrust.visitormanagement.model.Personne;
import ma.digitaltrust.visitormanagement.model.RendezVous;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.stream.IntStream;

@SpringBootApplication
public class VisitorManagementApplication implements CommandLineRunner {

	@Autowired
	private PersonneRepository personneRepository;
	@Autowired
	private RendezVousRepository rendezVousRepository;

	public static void main(String[] args) {
		ApplicationContext applicationContext =  SpringApplication.run(VisitorManagementApplication.class, args);
		Arrays.stream(applicationContext.getBeanDefinitionNames())
				.filter(bean -> applicationContext.getBean(bean).getClass().getPackage().getName().startsWith("ma.digitaltrust.visitormanagement"))
				.distinct()
				.forEach(System.out::println);
	}

	/*@Bean
	public CorsFilter corsFilter() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
		corsConfiguration.setAllowedHeaders(Arrays.asList("Origin", "Access-Control-Allow-Origin", "Content-Type",
				"Accept", "Authorization", "Origin, Accept", "X-Requested-With",
				"Access-Control-Request-Method", "Access-Control-Request-Headers"));
		corsConfiguration.setExposedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization",
				"Access-Control-Allow-Origin", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));
		corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
		urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
		return new CorsFilter(urlBasedCorsConfigurationSource);
	}*/

	@Override
	public void run(String... args) throws Exception {

		IntStream.range(1,2).forEach((i) -> {

			Personne personne = new Personne();
			personne.setNom("EZZAKRI");
			personne.setPrenom("Hamza");
			personne.setEmail("hamzaezzakri98@gmail.com");
			personne.setMatricule("Matricule " +i);
			personne.setOrganisation("Organisation " +i);
			personne.setDirection("Direction " +i);
			personne.setService("Service " +i);
			personneRepository.save(personne);

			RendezVous rendezVous = new RendezVous();
			rendezVous.setNom("Nom " +i);
			rendezVous.setPrenom("Prenom " +i);
			rendezVous.setIdCard("IdCard " +i);
			rendezVous.setEmail("zakri7740@gmail.com");
			rendezVous.setInvitePar("Hamza EZZAKRI");
			rendezVous.setEtat(Etat.EN_ATTENTE);
			rendezVous.setDateVisite(LocalDate.now().plusDays(3));
			rendezVous.setHeureVisite(LocalTime.now());
			rendezVous.setDroit(true);
			rendezVous.setPresence(true);
			rendezVousRepository.save(rendezVous);
		});
	}
}
