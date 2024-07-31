package gift.dto.Response;

public record CategoryResponseDto(
    Long id,
    String name,
    String color,
    String imageUrl,
    String description
) {}
