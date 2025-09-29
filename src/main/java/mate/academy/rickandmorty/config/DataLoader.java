package mate.academy.rickandmorty.config;

import lombok.RequiredArgsConstructor;
import mate.academy.rickandmorty.service.CharacterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);
    private final CharacterService characterService;

    @Override
    public void run(ApplicationArguments arguments) {

        try {
            characterService.loadInitialData();
        } catch (Exception e) {
            logger.error("Initial data load failed!: " + e);
        }
    }
}
