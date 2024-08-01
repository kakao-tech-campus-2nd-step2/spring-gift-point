package gift.dto.Request;

public record CategoryRequestDto(
    String name,
    String color,
    String imageUrl,
    String description
) {
}

