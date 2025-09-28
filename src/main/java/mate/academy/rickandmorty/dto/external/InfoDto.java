package mate.academy.rickandmorty.dto.external;

public record InfoDto(
        int count,
        int pages,
        String next,
        String previous
) {}
