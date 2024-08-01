package gift.main.dto;

import gift.main.entity.Product;

public record ProductResponce(
        Long id,
        String name,
        int price,
        String imageUrl,
        String categoryName) {

    public ProductResponce(Product product) {
        this(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                product.getCategoryName());

    }
}
