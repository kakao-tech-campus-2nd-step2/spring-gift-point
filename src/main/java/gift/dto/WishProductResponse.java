package gift.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import gift.entity.Product;

public class WishProductResponse {

    private Long id;
    private String name;
    private int price;

    @JsonProperty("image_url")
    private String imgUrl;

    public WishProductResponse(Long id, String name, int price, String imgUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imgUrl = imgUrl;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public static WishProductResponse from(Product product) {
        return new WishProductResponse(product.getId(), product.getName(), product.getPrice(), product.getImgUrl());
    }

}
