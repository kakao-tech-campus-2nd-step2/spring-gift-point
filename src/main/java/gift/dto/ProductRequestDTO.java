package gift.dto;

import gift.model.Category;
import gift.model.Product;
import jakarta.validation.constraints.*;

public class ProductRequestDTO {
    @NotBlank(message = "이름을 입력해주세요.")
    @Size(max = 15, message = "이름은 15자를 넘길 수 없습니다.")
    private String name;

    @NotBlank(message = "Image URL을 입력해주세요.")
    private String imageUrl;

    @NotNull(message = "가격을 입력해주세요.")
    @Min(value = 1, message = "가격은 1 미만이 될 수 없습니다.")
    private Integer price;

    @NotNull(message = "카테고리 ID를 입력해주세요.")
    @Min(value = 1, message = "카테고리 ID는 1 이상이어야 합니다.")
    private Long categoryId;

    public ProductRequestDTO() {
    }

    public ProductRequestDTO(String name, String imageUrl, Integer price, Long categoryId) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Integer getPrice() {
        return price;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public Product toEntity(Category category) {
        return new Product(this.name, this.price, this.imageUrl, category);
    }
}
