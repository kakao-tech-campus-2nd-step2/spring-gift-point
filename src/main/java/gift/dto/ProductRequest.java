package gift.dto;

import gift.entity.Category;
import gift.entity.Product;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public class ProductRequest {

    private Long id;

    @NotBlank(message = "상품의 이름은 필수 항목입니다.")
    @Size(max = 15, message = "상품의 이름은 최대 15자까지 입력할 수 있습니다.")
    private String name;

    @Min(value = 0, message = "상품의 가격은 0보다 크거나 같아야 합니다.")
    private int price;

    private String imgUrl;

    private Long categoryId;

    @NotNull(message = "옵션 리스트는 필수 항목입니다.")
    @Size(min = 1, message = "옵션은 최소 하나 이상이어야 합니다.")
    @Valid
    private List<OptionRequest> options;

    public ProductRequest() {
    }

    public ProductRequest(Long id, String name, int price, String imgUrl, Long categoryId) {
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

    public List<OptionRequest> getOptions() {
        return options;
    }

    public static ProductRequest from(Product product) {
        return new ProductRequest(product.getId(), product.getName(), product.getPrice(),
            product.getImgUrl(), product.getCategory().getId());
    }

    public static Product toEntity(ProductRequest request, Category category) {
        return new Product(request.getName(), request.getPrice(), request.getImgUrl(), category);
    }
}
