package gift.main.dto;

import gift.main.entity.WishProduct;

public record WishProductResponse(Long id, String name, int price, String imageUrl) {

    public WishProductResponse(WishProduct wishProduct) {
        this(
                wishProduct.product.getId(),
                wishProduct.product.getName(),
                wishProduct.product.getPrice(),
                wishProduct.product.getImageUrl()
        );

    }

}
