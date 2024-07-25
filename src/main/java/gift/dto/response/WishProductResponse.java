package gift.dto.response;

import gift.domain.Product;

public record WishProductResponse(String name, Integer price, String imageUrl) {
    public WishProductResponse(Product product) {
        this(product.getName(), product.getPrice(), product.getImageUrl());
    }
}
