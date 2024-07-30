package gift.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import gift.entity.Product;

@JsonPropertyOrder({"id", "name", "price", "image_url", "category_id"})
public class ProductUpdateResponse {

    private Long id;
    private String name;
    private int price;

    @JsonProperty("image_url")
    private String imgUrl;

    @JsonProperty("category_id")
    private Long categoryId;

    public ProductUpdateResponse() {}

    public ProductUpdateResponse(Long id, String name, int price, String imgUrl, Long categoryId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imgUrl = imgUrl;
        this.categoryId = categoryId;
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

    public Long getCategoryId() {
        return categoryId;
    }

    public static ProductUpdateResponse from(Product product) {
        return new ProductUpdateResponse(product.getId(), product.getName(), product.getPrice(),
            product.getImgUrl(), product.getCategory().getId());
    }

}
