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
import org.springframework.stereotype.Component;

@Component
public class RickAndMortyClient {
    private static final String BASE_URL = "https://rickandmortyapi.com/api/character";
    private final ObjectMapper objectMapper;

    public RickAndMortyClient(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    }

    public List<ExternalCharacterDto> fetchAllCharacters() {

        List<ExternalCharacterDto> allCharacters = new ArrayList<>();

        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BASE_URL))
                .build();
        try {
            HttpResponse<String> response = httpClient
                    .send(httpRequest, HttpResponse.BodyHandlers.ofString());

            System.out.println(response.body());

            RickAndMortyResponseDto apiResponse = objectMapper
                    .readValue(response.body(), RickAndMortyResponseDto.class);

            allCharacters.addAll(apiResponse.results());

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to fetch information from Rick and Morty API ", e);
        }
        return allCharacters;
    }
}
