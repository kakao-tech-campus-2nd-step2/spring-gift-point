package gift.DTO.Wish;

import gift.DTO.Product.ProductResponse;
import gift.DTO.User.UserResponse;
import gift.domain.WishProduct;

public class WishProductResponse {
    private Long wishId;
    private String name;
    private int price;
    private String imageUrl;

    public WishProductResponse(){

    }

    public WishProductResponse(WishProduct wishProduct) {
        this.wishId = wishProduct.getId();
        this.name = wishProduct.getProduct().getName();
        this.price = wishProduct.getProduct().getPrice();
        this.imageUrl = wishProduct.getProduct().getImageUrl();
    }

    public Long getWishId() {
        return wishId;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
