package gift.dto.Request;


public record ProductRequestDto(
    String name,
    Integer price,
    String imageUrl,
    Long categoryId
) {}
