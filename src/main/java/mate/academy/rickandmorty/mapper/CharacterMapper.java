package mate.academy.rickandmorty.mapper;

import mate.academy.rickandmorty.dto.CharacterResponseDto;
import mate.academy.rickandmorty.dto.external.ExternalCharacterDto;
import mate.academy.rickandmorty.model.Character;

public class CharacterMapper {
    public static CharacterResponseDto toDto(Character character) {
        return new CharacterResponseDto(
                character.getId(),
                character.getExternalId(),
                character.getName(),
                character.getStatus(),
                character.getGender()
        );
    }

    public static Character toEntity(ExternalCharacterDto externalCharacterDto) {
        Character character = new Character();
        character.setExternalId(externalCharacterDto.id().toString());
        character.setName(externalCharacterDto.name());
        character.setStatus(externalCharacterDto.status());
        character.setGender(externalCharacterDto.gender());
        return character;
    }
}
