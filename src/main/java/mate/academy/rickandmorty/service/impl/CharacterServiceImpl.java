package mate.academy.rickandmorty.service.impl;

import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import mate.academy.rickandmorty.client.RickAndMortyClient;
import mate.academy.rickandmorty.dto.CharacterResponseDto;
import mate.academy.rickandmorty.dto.external.ExternalCharacterDto;
import mate.academy.rickandmorty.model.Character;
import mate.academy.rickandmorty.repository.CharacterRepository;
import mate.academy.rickandmorty.service.CharacterService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CharacterServiceImpl implements CharacterService {

    private final CharacterRepository characterRepository;
    private final RickAndMortyClient rickAndMortyClient;
    private final Random random = new Random();

    @Override
    public void loadInitialData() {
        System.out.println("loadInitialData() виконується...");
        List<ExternalCharacterDto> externalCharacterDto = rickAndMortyClient.fetchAllCharacters();
        System.out.println("Отримано персонажів: " + externalCharacterDto.size());

        List<Character> toSaveDataEntity = externalCharacterDto.stream()
                .map(dto -> {
                    Character character = new Character();
                    character.setExternalId(dto.id().toString());
                    character.setName(dto.name());
                    character.setStatus(dto.status());
                    character.setGender(dto.gender());
                    return character;
                })
                .toList();

        characterRepository.saveAll(toSaveDataEntity);
        System.out.println("Збережено у БД " + toSaveDataEntity.size() + " персонажів.");
    }

    @Override
    public List<CharacterResponseDto> searchByName(String name) {
        return (name == null || name.isBlank())
                ? List.of() :
                characterRepository.findByNameContainingIgnoreCase(name).stream()
                        .map(this::toDto)
                        .toList();
    }

    @Override
    public CharacterResponseDto getRandomCharacter() {
        List<Character> characters = characterRepository.findAll();
        if (characters.isEmpty()) {
            return null;
        }
        Character characterEntity = characters.get(random.nextInt(characters.size()));
        return toDto(characterEntity);
    }

    private CharacterResponseDto toDto(Character character) {
        return new CharacterResponseDto(character.getId(),
                character.getExternalId(), character.getName(),
                character.getStatus(), character.getGender());
    }
}
