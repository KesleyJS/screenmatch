package br.com.kjscripts.screenmatch.main;

import br.com.kjscripts.screenmatch.model.EpisodeData;
import br.com.kjscripts.screenmatch.model.SeasonData;
import br.com.kjscripts.screenmatch.model.SerieData;
import br.com.kjscripts.screenmatch.service.ConsumeApi;
import br.com.kjscripts.screenmatch.service.ConvertData;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private ConsumeApi consumeApi = new ConsumeApi();
    private ConvertData convertData = new ConvertData();

    private Scanner reading = new Scanner(System.in);

    private final String CLIENT_URL = "https://omdbapi.com/?t=";
    private final String API_KEY = "&apikey=SuaChaveAqui";

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

        System.out.println("Lista de temporadas de " + serieName);
        seasons.forEach(System.out::println);


        seasons.forEach(s -> s.episodes().forEach(e -> System.out.println(e.title())));
    }
}
