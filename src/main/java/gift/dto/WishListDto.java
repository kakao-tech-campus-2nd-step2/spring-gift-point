package gift.dto;

import gift.domain.Wish;

public class WishListDto {
    private final Long id;
    private final Long product_id;
    private final String name;
    private final Integer price;
    private final String imageUrl;

    public WishListDto(Wish wish){
        this.id = wish.getWishId();
        this.product_id = wish.getProduct().getId();
        this.name = wish.getProduct().getName();
        this.price = wish.getProduct().getPrice();
        this.imageUrl = wish.getProduct().getImageUrl();
    }
    public Long getId() {
        return id;
    }
    public Long getProduct_id() {
        return product_id;
    }
    public String getName() {
        return name;
    }
    public Integer getPrice() {
        return price;
    }
    public String getImageUrl() {
        return imageUrl;
    }
}
