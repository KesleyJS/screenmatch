package br.com.kjscripts.screenmatch.repository;

import br.com.kjscripts.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {
    Optional<Serie> findByTitleContainingIgnoreCase(String serieName);

    List<Serie> findByActorsContainingIgnoreCaseAndRatingGreaterThanEqual(String actorName, double rating);
}
