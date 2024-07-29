package gift.dto;

import gift.model.Category;

public record ProductResponse(
        Long id,
        String name,
        int price,
        String imageUrl,
        Category category
) {
}
