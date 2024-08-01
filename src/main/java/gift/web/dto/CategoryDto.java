package gift.web.dto;

public record CategoryDto(
    Long id,
    String name,
    String color,
    String imageUrl,
    String description
)
{ }