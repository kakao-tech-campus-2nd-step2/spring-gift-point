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

    public Long getProductId() {
        return productId;
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
    public void setWishId(Long wishId){
        this.wishId = wishId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setName(String name) {
        this.name = name;
    }
}