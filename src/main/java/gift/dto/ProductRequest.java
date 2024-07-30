package gift.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import gift.entity.Category;
import gift.entity.Product;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

@JsonPropertyOrder({"name", "price", "image_url", "category_id", "options"})
public class ProductRequest {

    @NotBlank(message = "상품의 이름은 필수 항목입니다.")
    @Size(max = 15, message = "상품의 이름은 최대 15자까지 입력할 수 있습니다.")
    private String name;

    @Min(value = 0, message = "상품의 가격은 0보다 크거나 같아야 합니다.")
    private int price;

    @JsonProperty("image_url")
    private String imgUrl;

    @JsonProperty("category_id")
    private Long categoryId;

    @Valid
    @NotNull(message = "옵션 목록은 필수 항목입니다.")
    private List<OptionRequest> options;

    public ProductRequest() {
    }

    public ProductRequest(String name, int price, String imgUrl, Long categoryId) {
        this.name = name;
        this.price = price;
        this.imgUrl = imgUrl;
        this.categoryId = categoryId;
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

    public List<OptionRequest> getOptions() {
        return options;
    }

    public static ProductRequest from(Product product) {
        return new ProductRequest(product.getName(), product.getPrice(),
            product.getImgUrl(), product.getCategory().getId());
    }

    public static Product toEntity(ProductRequest request, Category category) {
        return new Product(request.getName(), request.getPrice(), request.getImgUrl(), category);
    }
}
