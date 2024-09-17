package br.com.kjscripts.screenmatch.service;

import br.com.kjscripts.screenmatch.model.TranslateData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URLEncoder;

public class GetMyMemory {
    public static String getTranslate(String text) {
        ObjectMapper mapper = new ObjectMapper();

        ConsumeApi consume = new ConsumeApi();

        String texto = URLEncoder.encode(text);
        String langpair = URLEncoder.encode("en|pt-br");

        String url = "https://api.mymemory.translated.net/get?q=" + texto + "&langpair=" + langpair;

        String json = consume.getData(url);

        TranslateData translate;
        try {
            translate = mapper.readValue(json, TranslateData.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return translate.responseData().translatedText();
    }
}