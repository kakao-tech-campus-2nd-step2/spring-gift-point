package gift.main.dto;

import gift.main.entity.WishProduct;

public record WishProductResponse(Long id,
                                  long product_id,
                                  String productName,
                                  String imageUrl) {

    public WishProductResponse(WishProduct wishProduct) {
        this(
                wishProduct.getId(),
                wishProduct.getProduct().getId(),
                wishProduct.product.getName(),
                wishProduct.getProduct().getImageUrl()
        );

    }

}
