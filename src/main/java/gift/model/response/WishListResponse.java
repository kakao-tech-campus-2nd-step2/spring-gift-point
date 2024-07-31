package gift.model.response;

import gift.model.entity.WishItem;

public class WishListResponse {

    private final Long wishId;
    private final Long productId;
    private final String name;
    private final Long price;
    private final String imgUrl;


    public WishListResponse(Long id, Long productId, String name, Long price, String imgUrl) {
        this.wishId = id;
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.imgUrl = imgUrl;
    }

    public WishListResponse(WishItem wishItem) {
        this.wishId = wishItem.getId();
        this.productId = wishItem.getItem().getId();
        this.name = wishItem.getItem().getName();
        this.price = wishItem.getItem().getPrice();
        this.imgUrl = wishItem.getItem().getImgUrl();
    }

    public Long getId() {
        return wishId;
    }

    public Long getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public String getImgUrl() {
        return imgUrl;
    }
}
