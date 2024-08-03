package gift.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import gift.entity.Category;
import gift.entity.Product;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@JsonPropertyOrder({"name", "price", "image_url", "category_id"})
public class ProductUpdateRequest {

    @NotBlank(message = "상품의 이름은 필수 항목입니다.")
    @Size(max = 15, message = "상품의 이름은 최대 15자까지 입력할 수 있습니다.")
    private String name;

    @Min(value = 0, message = "상품의 가격은 0보다 크거나 같아야 합니다.")
    private int price;

    @JsonProperty("image_url")
    private String imgUrl;

    @JsonProperty("category_id")
    private Long categoryId;

    public ProductUpdateRequest() {
    }

    public ProductUpdateRequest(String name, int price, String imgUrl, Long categoryId) {
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

    public static ProductUpdateRequest from(Product product) {
        return new ProductUpdateRequest(
            product.getName(),
            product.getPrice(),
            product.getImgUrl(),
            product.getCategory().getId()
        );
    }

    public static ProductUpdateRequest from(ProductRequest productRequest) {
        return new ProductUpdateRequest(
            productRequest.getName(),
            productRequest.getPrice(),
            productRequest.getImgUrl(),
            productRequest.getCategoryId()
        );
    }

    public static Product toEntity(ProductUpdateRequest request, Category category) {
        return new Product(
            request.getName(),
            request.getPrice(),
            request.getImgUrl(),
            category);
    }

}
