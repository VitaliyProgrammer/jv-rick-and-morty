package mate.academy.rickandmorty.dto.external;

public record ExternalCharacterDto(
        Integer id,
        String name,
        String status,
        String species,
        String type,
        String gender
) {}
