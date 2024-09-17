package br.com.kjscripts.screenmatch.repository;

import br.com.kjscripts.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SerieRepository extends JpaRepository<Serie, Long> {
}
