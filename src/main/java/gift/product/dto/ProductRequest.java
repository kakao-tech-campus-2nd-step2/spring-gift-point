package gift.product.dto;

import lombok.ToString;

@ToString
public class ProductRequest {

    private String name;
    private Integer price;
    private String imageUrl;
    private Long categoryId;
    private Long optionId;
    private String optionName;

    public ProductRequest() {
    }

    public ProductRequest(String name, Integer price, String imageUrl, Long categoryId) {
        this(name, price, imageUrl, categoryId, null, null);
    }

    public ProductRequest(String name, Integer price, String imageUrl, Long categoryId, String optionName) {
        this(name, price, imageUrl, categoryId, null, optionName);
    }

    public ProductRequest(String name, Integer price, String imageUrl, Long categoryId, Long optionId) {
        this(name, price, imageUrl, categoryId, optionId, null);
    }

    public ProductRequest(String name, Integer price, String imageUrl, Long categoryId, Long optionId,
        String optionName) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
        this.optionId = optionId;
        this.optionName = optionName;
    }

    public String getName() {
        return name;
    }

    public ProductRequest setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getPrice() {
        return price;
    }

    public ProductRequest setPrice(Integer price) {
        this.price = price;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public ProductRequest setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public ProductRequest setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
        return this;
    }

    public Long getOptionId() {
        return optionId;
    }

    public ProductRequest setOptionId(Long optionId) {
        this.optionId = optionId;
        return this;
    }
}

