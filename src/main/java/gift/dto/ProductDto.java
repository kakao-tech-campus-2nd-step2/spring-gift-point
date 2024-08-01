package gift.dto;

public class ProductDto {

    private String name;
    private Integer price;
    private String imageUrl;
    private Long categoryId;
    private Long optionId;
    private String optionName;

    public ProductDto() {
    }

    public ProductDto(String name, Integer price, String imageUrl, Long categoryId) {
        this(name, price, imageUrl, categoryId, null, null);
    }

    public ProductDto(String name, Integer price, String imageUrl, Long categoryId, String optionName) {
        this(name, price, imageUrl, categoryId, null, optionName);
    }

    public ProductDto(String name, Integer price, String imageUrl, Long categoryId, Long optionId) {
        this(name, price, imageUrl, categoryId, optionId, null);
    }

    public ProductDto(String name, Integer price, String imageUrl, Long categoryId, Long optionId,
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

    public ProductDto setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getPrice() {
        return price;
    }

    public ProductDto setPrice(Integer price) {
        this.price = price;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public ProductDto setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public ProductDto setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
        return this;
    }

    public Long getOptionId() {
        return optionId;
    }

    public ProductDto setOptionId(Long optionId) {
        this.optionId = optionId;
        return this;
    }
}

