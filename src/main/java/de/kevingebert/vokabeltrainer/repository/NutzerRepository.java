package de.kevingebert.vokabeltrainer.repository;

import de.kevingebert.vokabeltrainer.model.Nutzer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NutzerRepository extends JpaRepository<Nutzer, Long> {

    Optional<Nutzer> findByBenutzername(String benutzername);
}
