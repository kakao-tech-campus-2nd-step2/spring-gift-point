package gift.main.dto;

import gift.main.entity.WishProduct;

public record WishProductResponse(Long id, String name, int price, String imageUrl, String seller) {

    public WishProductResponse(WishProduct wishProduct) {
        this(
                wishProduct.product.getId(),
                wishProduct.product.getName(),
                wishProduct.product.getPrice(),
                wishProduct.product.getImageUrl(),
                wishProduct.product.getSellerName());

    }

}
