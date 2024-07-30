package gift.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import gift.entity.Product;
import java.util.List;

@JsonPropertyOrder({"id", "name", "price", "image_url", "category", "options"})
public class ProductResponse {

    private Long id;
    private String name;
    private int price;

    @JsonProperty("image_url")
    private String imgUrl;

    private CategoryResponse category;
    private List<OptionResponse> options;

    public ProductResponse() {
    }

    public ProductResponse(Long id, String name, int price, String imgUrl, CategoryResponse category,
        List<OptionResponse> options) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imgUrl = imgUrl;
        this.category = category;
        this.options = options;
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

    public CategoryResponse getCategory() {
        return category;
    }

    public List<OptionResponse> getOptions() {
        return options;
    }

    public static ProductResponse from(Product product) {
        CategoryResponse categoryResponse = CategoryResponse.from(product.getCategory());
        List<OptionResponse> optionResponses = product.getOptions().stream()
            .map(OptionResponse::from)
            .toList();

        return new ProductResponse(product.getId(), product.getName(), product.getPrice(),
            product.getImgUrl(), categoryResponse, optionResponses);
    }
}
