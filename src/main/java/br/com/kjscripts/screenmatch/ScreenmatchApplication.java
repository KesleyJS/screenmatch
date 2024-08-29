package br.com.kjscripts.screenmatch;

import br.com.kjscripts.screenmatch.model.SerieData;
import br.com.kjscripts.screenmatch.service.ConsumeApi;
import br.com.kjscripts.screenmatch.service.ConvertData;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var consumeApi = new ConsumeApi();
		var json = consumeApi.getData("https://omdbapi.com/?t=gilmore+girls&apikey=SuaKeyAqui");

		System.out.println(json);

		ConvertData converter = new ConvertData();
		var data = converter.getData(json, SerieData.class);
		System.out.println(data);
	}
}
