package gift.main.dto;

import gift.main.entity.Product;

public record ProductResponse(
        Long id,
        String name,
        int price,
        String imageUrl,
        Long categoryId) {

    public ProductResponse(Product product) {
        this(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                product.getCategory().getId());
    }
}
