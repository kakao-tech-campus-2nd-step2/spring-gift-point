package gift.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import gift.entity.Product;

@JsonPropertyOrder({"id", "name", "price", "image_url"})
public class ProductResponseWithoutCategoryId {

    private Long id;
    private String name;
    private int price;

    @JsonProperty("image_url")
    private String imgUrl;

    public ProductResponseWithoutCategoryId(Long id, String name, int price, String imgUrl) {
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

    public static ProductResponseWithoutCategoryId from(Product product) {
        return new ProductResponseWithoutCategoryId(product.getId(), product.getName(),
            product.getPrice(),
            product.getImgUrl());
    }

}
