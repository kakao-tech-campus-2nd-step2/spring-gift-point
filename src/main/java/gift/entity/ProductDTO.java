package gift.entity;

import gift.validation.constraint.NameConstraint;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public class ProductDTO {

    @NameConstraint
    @NotNull
    @Schema(description = "상품명", nullable = false, example = "상품명 입니다")
    private String name;
    @NotNull
    @Schema(description = "상품 가격", nullable = false, example = "10000")
    private Integer price;
    @NotNull
    @Schema(description = "상품 이미지 url", nullable = false, example = "https://www.test.com")
    private String imageurl;
    @NotNull
    @Schema(description = "상품 카테고리", nullable = false, example = "1")
    private Long category_id;

    public ProductDTO(String name, Integer price, String imageurl, Long category_id) {
        this.name = name;
        this.price = price;
        this.imageurl = imageurl;
        this.category_id = category_id;
    }

    public ProductDTO() {
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

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public Long getCategoryid() {
        return category_id;
    }

    public void setCategoryid(Long category_id) {
        this.category_id = category_id;
    }
}
