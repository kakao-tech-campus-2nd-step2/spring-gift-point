package gift.product.dto;

public record WishOptionResponse(
        Long id,
        String name,
        Integer quantity,
        GetProductResponse product
) { }
