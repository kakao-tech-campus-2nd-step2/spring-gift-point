package gift.dto.product;

import gift.validation.constraint.NameConstraint;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public class ProductRequestDto {

    @NameConstraint
    @NotNull
    @Schema(description = "상품명", nullable = false, example = "상품명 입니다")
    private String name;
    @NotNull
    @Schema(description = "상품 가격", nullable = false, example = "10000")
    private Integer price;
    @NotNull
    @Schema(description = "상품 이미지 url", nullable = false, example = "https://www.test.com")
    private String imageUrl;
    @NotNull
    @Schema(description = "상품 카테고리", nullable = false, example = "1")
    private Long category_id;

    public ProductRequestDto(String name, Integer price, String imageUrl, Long category_id) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category_id = category_id;
    }

    public ProductRequestDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Long category_id) {
        this.category_id = category_id;
    }
}
