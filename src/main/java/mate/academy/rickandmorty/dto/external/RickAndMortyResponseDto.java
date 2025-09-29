package mate.academy.rickandmorty.dto.external;

import java.util.List;

public record RickAndMortyResponseDto(
        InfoDto info,
        List<ExternalCharacterDto> results
) {}
