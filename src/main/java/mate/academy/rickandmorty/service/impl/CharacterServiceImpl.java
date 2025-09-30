package mate.academy.rickandmorty.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.rickandmorty.client.RickAndMortyClient;
import mate.academy.rickandmorty.dto.CharacterResponseDto;
import mate.academy.rickandmorty.dto.external.ExternalCharacterDto;
import mate.academy.rickandmorty.mapper.CharacterMapper;
import mate.academy.rickandmorty.model.Character;
import mate.academy.rickandmorty.repository.CharacterRepository;
import mate.academy.rickandmorty.service.CharacterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CharacterServiceImpl implements CharacterService {

    private static final Logger logger = LoggerFactory.getLogger(CharacterServiceImpl.class);
    private final CharacterRepository characterRepository;
    private final RickAndMortyClient rickAndMortyClient;
    private final Random random = new Random();

    @Override
    @Transactional
    public void loadInitialData() {

        logger.info("Starting initial data load from external API...");
        List<ExternalCharacterDto> externalCharacterDto = rickAndMortyClient.fetchAllCharacters();
        logger.info("Received {} characters", externalCharacterDto.size());

        Set<String> existingExternalIds = new HashSet<>(characterRepository.findByExternalId());

        List<Character> toSaveInitialData = externalCharacterDto.stream()
                .filter(dto -> !existingExternalIds.contains(dto.id().toString()))
                .map(CharacterMapper::toEntity)
                .toList();

        if (toSaveInitialData.isEmpty()) {
            logger.info("No new characters to save.");
            return;
        }

        logger.info("Saving {} new characters", toSaveInitialData.size());

        characterRepository.saveAll(toSaveInitialData);
        logger.info("Initial data load finished.");
    }

    @Override
    public List<CharacterResponseDto> searchByName(String name) {
        return (name == null || name.isBlank())
                ? List.of() :
                characterRepository.findByNameContainingIgnoreCase(name).stream()
                        .map(CharacterMapper::toDto)
                        .toList();
    }

    @Override
    public Optional<CharacterResponseDto> getRandomCharacter() {

        long count = characterRepository.count();
        if (count == 0) {
            return Optional.empty();
        }

        int randomIndex = random.nextInt((int) count);
        Page<Character> page = characterRepository.findAll(PageRequest.of(randomIndex, 1));

        List<Character> existsContent = page.getContent();
        if (existsContent.isEmpty()) {
            return Optional.empty();
        }

        Character character = page.getContent().get(0);
        return Optional.of(CharacterMapper.toDto(character));
    }
}
