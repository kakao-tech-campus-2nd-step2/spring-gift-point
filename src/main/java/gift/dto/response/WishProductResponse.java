package gift.dto.response;

import gift.domain.Product;

public record WishProductResponse(Long wishId, String name, Integer price, String imageUrl) {
    public WishProductResponse(Long wishId, Product product) {
        this(wishId, product.getName(), product.getPrice(), product.getImageUrl());
    }
}
