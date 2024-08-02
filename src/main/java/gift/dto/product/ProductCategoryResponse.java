package gift.dto.product;

import gift.dto.option.OptionResponse;

import java.util.List;

public class ProductCategoryResponse {
    private Long id;
    private Long categoryId;
    private String name;
    private int price;
    private String imageUrl;
    private List<OptionResponse> options;

    public ProductCategoryResponse(Long id, Long categoryId, String name, int price, String imageUrl, List<OptionResponse> options) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.options = options;
    }
    public Long getId() {
        return id;
    }
    public Long getCategoryId() {
        return categoryId;
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
    public List<OptionResponse> getOptions() {
        return options;
    }
}
