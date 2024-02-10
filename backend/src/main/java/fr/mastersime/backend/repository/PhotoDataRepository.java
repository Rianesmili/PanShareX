package fr.mastersime.backend.repository;

import fr.mastersime.backend.model.PhotoData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoDataRepository extends JpaRepository<PhotoData, Long> {
}