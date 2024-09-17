package br.com.kjscripts.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SerieData(@JsonAlias("Title") String title,
                        Integer totalSeasons,
                        @JsonAlias("imdbRating") String rating,
                        @JsonAlias("Genre")String genre,
                        @JsonAlias("Actors")String actors,
                        @JsonAlias("Poster")String poster,
                        @JsonAlias("Plot")String synopsis) {

}
