package gift.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import gift.entity.Product;

public class ProductInfo {
    
    private Long id;
    private String name;
    private int price;
    private String imageUrl;

    @JsonCreator
    public ProductInfo(
        @JsonProperty("id") Long id,
        @JsonProperty("name") String name,
        @JsonProperty("price") int price,
        @JsonProperty("image_url") String imageUrl
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public static ProductInfo fromEntity(Product product){
        return new ProductInfo(product.getId(), product.getName(), product.getPrice(), product.getImageUrl());
    }
}
