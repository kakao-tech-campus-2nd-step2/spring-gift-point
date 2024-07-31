package gift.dto.response;

import gift.domain.Product;

public record ProductResponse(Long id, String name, Integer price, String imageUrl) {
    public ProductResponse(Product product) {
        this(product.getId(), product.getName(), product.getPrice(), product.getImageUrl());
    }
}
