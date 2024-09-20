package br.com.kjscripts.screenmatch.main;

import br.com.kjscripts.screenmatch.model.Episode;
import br.com.kjscripts.screenmatch.model.SeasonData;
import br.com.kjscripts.screenmatch.model.Serie;
import br.com.kjscripts.screenmatch.model.SerieData;
import br.com.kjscripts.screenmatch.repository.SerieRepository;
import br.com.kjscripts.screenmatch.service.ConsumeApi;
import br.com.kjscripts.screenmatch.service.ConvertData;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private ConsumeApi consumeApi = new ConsumeApi();
    private ConvertData convertData = new ConvertData();

    private Scanner reading = new Scanner(System.in);

    private final String CLIENT_URL = "https://omdbapi.com/?t=";
    private final String API_KEY = "&apikey=5d262c1a";

    private List<SerieData> seriesData = new ArrayList<>();

    private SerieRepository serieRepository;

    private String apiKey;

    List<Serie> series = new ArrayList<>();

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
                    4 - Buscar série pelo nome
                    5 - Buscar séries pelo ator
                    6 - Buscar as 5 melhores séries
                    
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
                case 4:
                    findSerieByTitle();
                    break;
                case 5:
                    getSeriesByActor();
                    break;
                case 6:
                    getTopFiveSeries();
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
        listFetchedSeries();
        System.out.println("Digite o nome de uma série: ");
        var serieName = reading.nextLine();

        Optional<Serie> serie = series.stream()
                .filter(s -> s.getTitle().toLowerCase().contains(serieName.toLowerCase()))
                .findFirst();

        if (serie.isPresent()) {

            var fetchedSerie = serie.get();
            List<SeasonData> seasons = new ArrayList<>();

            for (int i = 1; i <= fetchedSerie.getTotalSeasons(); i++) {
                // Encode the serieTitle for safe inclusion in the URL
                String encodedSerieTitle = URLEncoder.encode(fetchedSerie.getTitle(), StandardCharsets.UTF_8);
                var json = consumeApi.getData(CLIENT_URL + encodedSerieTitle + "&season=" + i + "&apikey=" + apiKey);
                SeasonData seasonData = convertData.getData(json, SeasonData.class);
                seasons.add(seasonData);
            }
            seasons.forEach(System.out::println);

            List<Episode> episodes = seasons.stream()
                    .flatMap(s -> s.episodes().stream()
                            .map(e -> new Episode(s.number(), e)))
                    .collect(Collectors.toList());

            fetchedSerie.setEpisodes(episodes);

            serieRepository.save(fetchedSerie);
        } else {
            System.out.println("Série não encontrada");
        }
    }

    private void listFetchedSeries() {
        series = serieRepository.findAll();

        series.stream()
                .sorted(Comparator.comparing(Serie::getGenre))
                .forEach(System.out::println);
    }

    private void findSerieByTitle() {
        System.out.println("Digite o nome de uma série");
        var serieName = reading.nextLine();

        Optional<Serie> fetchedSerie = serieRepository.findByTitleContainingIgnoreCase(serieName);

        if (fetchedSerie.isPresent()) {
            System.out.println("Dados da série: \n" + fetchedSerie.get());
        } else  {
            System.out.println("Série não encontrada");
        }
    }

    private void getSeriesByActor() {
        System.out.println("Digite o nome de um ator");
        var actorName = reading.nextLine();

        System.out.println("Avaliações a partir de qual valor?");
        var rating = reading.nextDouble();

        List<Serie> fetchedSeries = serieRepository.findByActorsContainingIgnoreCaseAndRatingGreaterThanEqual(actorName, rating);

        System.out.println("Séries que " + actorName + " atuou");
        fetchedSeries.forEach((s ->
                System.out.println(s.getTitle() + " avaliação: " + s.getRating())));
    }


    private void getTopFiveSeries() {
        List<Serie> topFiveSeries = serieRepository.findTop5ByOrderByRatingDesc();

        topFiveSeries.forEach((s ->
                System.out.println(s.getTitle() + " avaliação: " + s.getRating())));
    }

}

