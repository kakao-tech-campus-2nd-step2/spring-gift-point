package gift.dto;

import gift.entity.Wish;

public class WishResponseDto {

    private final Long id;
    private final String name;
    private final double price;
    private final String imageUrl;

    public WishResponseDto(Wish wish) {
        this.id = wish.getId();
        this.name = wish.getProduct().getName();
        this.price = wish.getProduct().getPrice();
        this.imageUrl = wish.getProduct().getImageUrl();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}