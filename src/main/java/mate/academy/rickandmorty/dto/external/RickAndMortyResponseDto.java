package mate.academy.rickandmorty.dto.external;

import io.swagger.v3.oas.models.info.Info;
import java.util.List;

public record RickAndMortyResponseDto(
        Info info,
        List<ExternalCharacterDto> results
) {}
