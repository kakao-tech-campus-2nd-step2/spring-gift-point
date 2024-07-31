package gift.dto;

import gift.domain.Wish;

public class WishListDto {
    private Long id;
    private Long product_id;
    private String name;
    private Integer price;
    private String imageUrl;

    public WishListDto(Wish wish){
        this.id = wish.getWishId();
        this.product_id = wish.getProduct().getId();
        this.name = wish.getProduct().getName();
        this.price = wish.getProduct().getPrice();
        this.imageUrl = wish.getProduct().getImageUrl();
    }
}
