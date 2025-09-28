package mate.academy.rickandmorty.dto;

public record CharacterResponseDto(
        Long id,
        String externalId,
        String name,
        String status,
        String gender
) {}
