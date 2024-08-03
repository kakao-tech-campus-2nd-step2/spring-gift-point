package gift.web.dto.category;

public record CategoryResponseDto(
    Long id,
    String name,
    String color,
    String imageUrl,
    String description
)
{ }