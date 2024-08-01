package gift.dto;

public record CategoryRequestDto(
        String name,
        String color,
        String imageUrl,
        String description
) {
}
