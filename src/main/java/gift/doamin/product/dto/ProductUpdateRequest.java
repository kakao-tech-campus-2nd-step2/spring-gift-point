package gift.doamin.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

@Schema(description = "상품 수정 요청")
public class ProductUpdateRequest {

    @Schema(description = "상품의 카테고리 id")
    @NotNull
    private Long categoryId;

    @Schema(description = "상품명")
    @Pattern(regexp = "^[a-zA-Zㄱ-ㅎㅏ-ㅣ가-힣0-9 ()\\[\\]+\\-&/_]{1,15}$", message = "영문, 한글, 숫자, 공백, 특수문자 ()[]+-&/_ 1자 이상 15자 미만으로 입력해야 합니다.")
    private String name;

    @Schema(description = "상품 가격")
    @NotNull
    @PositiveOrZero
    private Integer price;

    @Schema(description = "상품 이미지 url")
    @NotNull
    private String imageUrl;

    public ProductUpdateRequest(Long categoryId, String name, Integer price, String imageUrl) {
        this.categoryId = categoryId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}