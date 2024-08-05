package gift.dto.response;

import gift.entity.Wishlist;

public class WishlistResponse {

    private Long wishId;
    private Long productId;
    private String name;
    private int price;
    private String imageUrl;


    public WishlistResponse(Wishlist wishlist) {
        this.wishId = wishlist.getId();
        this.productId = wishlist.getProduct().getId();
        this.name = wishlist.getProduct().getName();
        this.price = wishlist.getProduct().getPrice();
        this.imageUrl = wishlist.getProduct().getImageUrl();
    }

    public Long getWishId() {
        return wishId;
    }

    public void setWishId(Long wishId) {
        this.wishId = wishId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}