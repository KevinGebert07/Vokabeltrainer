package de.kevingebert.vokabeltrainer.repository;

import de.kevingebert.vokabeltrainer.model.Vokabel;
import de.kevingebert.vokabeltrainer.model.Vokabelliste;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VokabelRepository extends JpaRepository<Vokabel, Long> {

    List<Vokabel> findByListe(Vokabelliste liste);
}
