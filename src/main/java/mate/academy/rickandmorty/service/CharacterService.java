package mate.academy.rickandmorty.service;

import java.util.List;
import java.util.Optional;
import mate.academy.rickandmorty.dto.CharacterResponseDto;

public interface CharacterService {
    void loadInitialData();

    List<CharacterResponseDto> searchByName(String name);

    Optional<CharacterResponseDto> getRandomCharacter();
}
