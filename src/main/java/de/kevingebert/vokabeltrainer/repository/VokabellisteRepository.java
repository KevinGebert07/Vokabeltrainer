package de.kevingebert.vokabeltrainer.repository;

import de.kevingebert.vokabeltrainer.model.Vokabelliste;
import de.kevingebert.vokabeltrainer.model.Nutzer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VokabellisteRepository extends JpaRepository<Vokabelliste, Long> {

    Optional<Vokabelliste> findByNutzerAndVonSpracheAndZuSprache(Nutzer nutzer,
                                                                 String vonSprache,
                                                                 String zuSprache);
}
