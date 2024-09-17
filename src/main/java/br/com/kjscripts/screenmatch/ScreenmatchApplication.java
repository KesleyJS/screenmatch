package br.com.kjscripts.screenmatch;

import br.com.kjscripts.screenmatch.main.Main;
import br.com.kjscripts.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	@Autowired
	private SerieRepository serieRepository;

	@Value("${API_KEY_OMDB}")
	private String apiKey;

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Main main = new Main(apiKey, serieRepository);
		main.showMenu();
	}
}
