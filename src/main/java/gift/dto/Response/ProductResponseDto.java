package gift.dto.Response;

public record ProductResponseDto(
    Long id,
    String name,
    int price,
    String imageUrl,
    CategoryResponseDto category
) {}
