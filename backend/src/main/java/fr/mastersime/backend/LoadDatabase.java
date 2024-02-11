package fr.mastersime.backend;

import fr.mastersime.backend.model.Location;
import fr.mastersime.backend.model.PhotoData;
import fr.mastersime.backend.repository.PhotoDataRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(PhotoDataRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                Location location1 = new Location(40.748817, -73.985428);
                Location location2 = new Location(51.507351, -0.127758);
                repository.save(new PhotoData("image1", location1, "Panneau de Ville"));
                repository.save(new PhotoData("image2", location2, "Panneau du code de la route"));
            }
        };
    }
}
