package gift.dto;

import gift.model.Category;

public record WishResponse(
        Long wishId,
        Long productId,
        String productName,
        int productPrice,
        String productImageUrl,
        Category category
) {
}
