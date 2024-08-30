package br.com.kjscripts.screenmatch.main;

import br.com.kjscripts.screenmatch.model.Episode;
import br.com.kjscripts.screenmatch.model.EpisodeData;
import br.com.kjscripts.screenmatch.model.SeasonData;
import br.com.kjscripts.screenmatch.model.SerieData;
import br.com.kjscripts.screenmatch.service.ConsumeApi;
import br.com.kjscripts.screenmatch.service.ConvertData;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    private ConsumeApi consumeApi = new ConsumeApi();
    private ConvertData convertData = new ConvertData();

    private Scanner reading = new Scanner(System.in);

    private final String CLIENT_URL = "https://omdbapi.com/?t=";
    private final String API_KEY = "&apikey=ApiKeyAqui";

    public void showMenu() {
        System.out.println("Digite o nome da série para buscar:");
        var serieName = reading.nextLine();
        // Encode the serieName for safe inclusion in the URL
        String encodedSerieName = URLEncoder.encode(serieName, StandardCharsets.UTF_8);

        var json = consumeApi.getData(CLIENT_URL + encodedSerieName + API_KEY);
        var serieData = convertData.getData(json, SerieData.class);
        System.out.println(serieData);

        List<SeasonData> seasons =  new ArrayList<>();

		for (int i = 1; i <= serieData.totalSeasons(); i++) {
			json = consumeApi.getData(CLIENT_URL + encodedSerieName + "&season=" + i + API_KEY);
			SeasonData seasonData = convertData.getData(json, SeasonData.class);
			seasons.add(seasonData);
		}

        System.out.println("Lista de temporadas de " + serieData.title());
        seasons.forEach(System.out::println);


        seasons.forEach(s -> s.episodes().forEach(e -> System.out.println(e.title())));

        List<EpisodeData> episodesData = seasons.stream()
                .flatMap(s -> s.episodes().stream())
                .collect(Collectors.toList());

        System.out.println("Os 5 melhores episódios de " +  serieData.title());
        episodesData.stream()
                .filter(e -> !e.rating().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(EpisodeData::rating).reversed())
                .limit(5)
                .forEach(System.out::println);

        List<Episode> episodes = seasons.stream()
                .flatMap(s -> s.episodes().stream()
                        .map(d -> new Episode(s.number(), d)))
                .collect(Collectors.toList());

        episodes.forEach(System.out::println);
    }
}
