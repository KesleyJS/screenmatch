package br.com.kjscripts.screenmatch.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Entity
@Table(name = "episodes")
public class Episode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer season;
    private String title;
    private Integer number;
    private Double rating;
    private LocalDate releaseDate;
    @ManyToOne
    private Serie serie;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public Episode() {}
    public Episode(Integer numberSeason, EpisodeData episodeData) {
        this.season = numberSeason;
        this.title = episodeData.title();
        this.number = episodeData.number();
        try {
            this.rating = Double.valueOf(episodeData.rating());
        } catch (NumberFormatException e) {
            this.rating = 0.0;
        }
        try {
            this.releaseDate = LocalDate.parse(episodeData.releaseDate());
        } catch (DateTimeParseException e) {
            this.releaseDate = null;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Serie getSerie() {
        return serie;
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
    }

    public Integer getSeason() {
        return season;
    }

    public void setSeason(Integer season) {
        season = season;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String formatReleaseDate(LocalDate date) {
        if (date != null) {
            return DATE_FORMATTER.format(date);
        } else {
            return "Indisponível";
        }

    }

    @Override
    public String toString() {

        return "\nTemporada: " + season
                + "\nEpisódio: " + number
                + "\nTítulo: " + title
                + "\nAvaliação" + rating
                + "\nData de lançamento: " + formatReleaseDate(releaseDate) + "\n";
    }
}
