package gift.dto;

import gift.domain.wish.Wish;

public class WishResponseDto {
    private final long wishId;
    private final long productId;
    private final Integer productPrice;
    private final String productImageUrl;
    private final String productName;

    public WishResponseDto(Wish wish) {
        this.wishId = wish.getId();
        this.productId = wish.getProduct().getId();
        this.productPrice = wish.getProduct().getPrice();
        this.productImageUrl = wish.getProduct().getImgUrl();
        this.productName = wish.getProduct().getName();
    }

    public Long getWishId() {
        return wishId;
    }


    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getProductPrice() {
        return productPrice;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }
}
