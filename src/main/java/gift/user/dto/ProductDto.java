package gift.user.dto;

import gift.product.entity.Product;

public record ProductDto(
    Long id,
    String name,
    Integer price,
    String imageUrl
) {

    public static ProductDto from(Product product) {
        return new ProductDto(product.getId(), product.getName(), product.getPrice(),
            product.getImageUrl());
    }

}
