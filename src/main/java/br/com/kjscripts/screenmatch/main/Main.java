package br.com.kjscripts.screenmatch.main;

import br.com.kjscripts.screenmatch.model.SeasonData;
import br.com.kjscripts.screenmatch.model.Serie;
import br.com.kjscripts.screenmatch.model.SerieData;
import br.com.kjscripts.screenmatch.repository.SerieRepository;
import br.com.kjscripts.screenmatch.service.ConsumeApi;
import br.com.kjscripts.screenmatch.service.ConvertData;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Main {

    private ConsumeApi consumeApi = new ConsumeApi();
    private ConvertData convertData = new ConvertData();

    private Scanner reading = new Scanner(System.in);

    private final String CLIENT_URL = "https://omdbapi.com/?t=";
    private final String API_KEY = "&apikey=5d262c1a";

    private List<SerieData> seriesData = new ArrayList<>();

    private SerieRepository serieRepository;

    private String apiKey;

    public Main(String apiKey, SerieRepository serieRepository) {
        this.apiKey = apiKey;
        this.serieRepository = serieRepository;
    }

    public void showMenu() {
        var choice = -1;
        while (choice != 0) {
            var menu = """
                1 - Buscar séries
                2 - Buscar episódios
                3 - Listar séries buscadas
                
                0 - Sair
                """;

            System.out.println(menu);
            choice = reading.nextInt();
            reading.nextLine();

            switch (choice) {
                case 1:
                    getSerieWeb();
                    break;
                case 2:
                    getEpisodeBySerie();
                    break;
                case 3:
                    listFetchedSeries();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void getSerieWeb() {
        SerieData serieData = getSerieData();
        Serie serie = new Serie(serieData);
//        seriesData.add(serieData);
        serieRepository.save(serie);
        System.out.println(serieData);
    }

    private SerieData getSerieData() {
        System.out.println("Digite o nome da série para busca");
        var serieName = reading.nextLine();
        // Encode the serieName for safe inclusion in the URL
        String encodedSerieName = URLEncoder.encode(serieName, StandardCharsets.UTF_8);
        var json = consumeApi.getData(CLIENT_URL + encodedSerieName + "&apikey=" + apiKey);
        SerieData data = convertData.getData(json, SerieData.class);
        return data;
    }

    private void getEpisodeBySerie() {
        SerieData serieData = getSerieData();
        List<SeasonData> seasons = new ArrayList<>();

        for (int i = 1; i <= serieData.totalSeasons(); i++) {
            // Encode the serieTitle for safe inclusion in the URL
            String encodedSerieTitle = URLEncoder.encode(serieData.title(), StandardCharsets.UTF_8);
            var json = consumeApi.getData(CLIENT_URL + encodedSerieTitle + "&season=" + i + "&apikey=" + apiKey);
            SeasonData seasonData = convertData.getData(json, SeasonData.class);
            seasons.add(seasonData);
        }
        seasons.forEach(System.out::println);
    }

    private void listFetchedSeries() {
        List<Serie> series = serieRepository.findAll();

        series.stream()
                .sorted(Comparator.comparing(Serie::getGenre))
                .forEach(System.out::println);
    }
}

