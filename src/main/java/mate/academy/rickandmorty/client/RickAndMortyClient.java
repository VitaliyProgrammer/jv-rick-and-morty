package mate.academy.rickandmorty.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import mate.academy.rickandmorty.dto.external.ExternalCharacterDto;
import mate.academy.rickandmorty.dto.external.RickAndMortyResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RickAndMortyClient {
    private static final Logger logger =
            LoggerFactory.getLogger(RickAndMortyClient.class);

    private static final String BASE_URL = "https://rickandmortyapi.com/api/character";
    private static final int MAX_RETRIES = 3;
    private final ObjectMapper objectMapper;

    public RickAndMortyClient(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    }

    public List<ExternalCharacterDto> fetchAllCharacters() {

        List<ExternalCharacterDto> allCharacters = new ArrayList<>();

        HttpClient httpClient = HttpClient.newHttpClient();

        String pageUrl = BASE_URL;

        while (pageUrl != null) {
            int attempt = 1;

            while (attempt <= MAX_RETRIES) {
                try {
                    HttpRequest httpRequest = HttpRequest.newBuilder()
                            .GET()
                            .uri(URI.create(BASE_URL))
                            .build();

                    HttpResponse<String> response = httpClient
                            .send(httpRequest, HttpResponse.BodyHandlers.ofString());

                    logger.debug("Fetched URL: {}, Status code: {}",
                            BASE_URL, response.statusCode());

                    if (response.statusCode() != 200) {
                        throw new RuntimeException("Failed to fetch characters. URL: "
                                + " Status code: " + response.statusCode()
                                + " Body: " + response.body());
                    }

                    RickAndMortyResponseDto apiResponse = objectMapper
                            .readValue(response.body(), RickAndMortyResponseDto.class);

                    allCharacters.addAll(apiResponse.results());
                    pageUrl = apiResponse.info().next();
                    break;

                } catch (IOException | InterruptedException e) {
                    logger.warn("Attempt {}/{} failed for URL: {}",
                            attempt, MAX_RETRIES, pageUrl, e);

                    if (attempt == MAX_RETRIES) {
                        throw new RuntimeException("Failed after " + MAX_RETRIES
                                + " attempts for URL: " + BASE_URL, e);
                    }
                }
                attempt++;

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Thread interrupted during retry", e);
                }
            }
        }
        return allCharacters;
    }
}
