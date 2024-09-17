package br.com.kjscripts.screenmatch.model;

import br.com.kjscripts.screenmatch.service.GetMyMemory;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

@Entity
@Table(name = "series")
public class Serie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String title;
    private Integer totalSeasons;
    private Double rating;
    @Enumerated(EnumType.STRING)
    private Category genre;
    private String actors;
    private String poster;
    private String synopsis;

    @Transient
    private List<Episode> episodes = new ArrayList<>();

    public Serie() {}

    public Serie(SerieData serieData) {
        this.title = serieData.title();
        this.totalSeasons = serieData.totalSeasons();
        this.rating = OptionalDouble.of(Double.valueOf(serieData.rating())).orElse(0);
        this.genre = Category.fromString(serieData.genre().split(",")[0].trim());
        this.actors = serieData.actors();
        this.poster = serieData.poster();
        this.synopsis = GetMyMemory.getTranslate(serieData.synopsis().trim());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTotalSeasons() {
        return totalSeasons;
    }

    public void setTotalSeasons(Integer totalSeasons) {
        this.totalSeasons = totalSeasons;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Category getGenre() {
        return genre;
    }

    public void setGenre(Category genre) {
        this.genre = genre;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
    }

    @Override
    public String toString() {
        return
                "\nGênero: " + genre +
                "\nTítulo: " + title +
                "\nNúmero de temporadas: " + totalSeasons +
                "\nAvaliação: " + rating +
                "\nAtores: " + actors +
                "\nPoster: " + poster +
                "\nSinópse: " + synopsis;
    }
}
