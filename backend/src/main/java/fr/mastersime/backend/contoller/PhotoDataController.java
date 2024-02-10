package fr.mastersime.backend.contoller;

import fr.mastersime.backend.model.PhotoData;
import fr.mastersime.backend.repository.PhotoDataRepository;
import fr.mastersime.backend.service.PhotoDataService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/photos")
public class PhotoDataController {

    private final PhotoDataRepository repository;

    public PhotoDataController(PhotoDataRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<PhotoData> getAllPhotos() {
        return repository.findAll();
    }
}
