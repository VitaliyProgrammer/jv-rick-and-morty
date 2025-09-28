package mate.academy.rickandmorty.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import mate.academy.rickandmorty.service.CharacterService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader {
    private final CharacterService characterService;

    @PostConstruct
    public void init() {
        System.out.println("DataLoader запускається...");
        characterService.loadInitialData();
    }
}
