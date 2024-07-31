package gift.product.dto;

public record GetProductResponse(
        Long id,
        String name,
        Integer price,
        String imageUrl,
        CategoryResponse category
) { }
