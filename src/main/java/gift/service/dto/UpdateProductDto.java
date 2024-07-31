package gift.service.dto;

public record UpdateProductDto(
        Long id,
        String name,
        int price,
        String imageUrl,
        Long categoryId
) {
}
