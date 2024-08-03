package gift.controller.product.dto;

import gift.model.Product;

public record ProductResponse(Long productId, String name, int price, String imageUrl) {

    public static ProductResponse from(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(),
            product.getImageUrl());
    }
}
