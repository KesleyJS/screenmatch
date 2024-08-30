package br.com.kjscripts.screenmatch.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConvertData implements IConvertData {

    /**
     * An instance of ObjectMapper used for converting JSON data to Java objects.
     */
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Converts a JSON String to a Java object of the specified type.
     *
     * @param json The JSON String representation of the data.
     * @param tClass The class type of the desired Java object.
     * @param <T> The generic type representing the Java object class.
     * @return The converted Java object from the provided JSON data.
     * @throws RuntimeException  If a JsonProcessingException occurs during conversion.
     */
    @Override
    public <T> T getData(String json, Class<T> tClass) {
        try {
            /**
             * Uses the ObjectMapper to read the JSON String and convert it to an object of the specified class type.
             */
            return mapper.readValue(json, tClass);
        } catch (JsonProcessingException e) {
            /**
             * Handles JsonProcessingException if an error occurs during conversion.
             * Re-throws as a RuntimeException for easier handling by the caller.
             */
            throw new RuntimeException(e);
        }
    }
}