package mate.academy.rickandmorty.mapper;

import mate.academy.rickandmorty.dto.CharacterResponseDto;
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

    public static Character toEntity(CharacterResponseDto characterResponseDto) {
        Character character = new Character();
        character.setExternalId(characterResponseDto.externalId());
        character.setName(characterResponseDto.name());
        character.setStatus(characterResponseDto.status());
        character.setGender(characterResponseDto.gender());
        return character;
    }
}
