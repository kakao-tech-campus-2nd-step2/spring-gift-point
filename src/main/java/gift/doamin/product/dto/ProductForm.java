package gift.doamin.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.List;

@Schema(description = "상품 등록, 수정 요청")
public class ProductForm {

    @Schema(description = "등록한 사용자 id")
    private Long userId;

    @Schema(description = "상품의 카테고리 id")
    @NotNull
    private Long category_id;

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

    @Schema(description = "상품의 옵션 목록")
    @NotEmpty
    @Valid
    private List<OptionForm> options;

    public ProductForm(Long category_id, String name, Integer price, String imageUrl,
        List<OptionForm> options) {
        this.category_id = category_id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.options = options;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getCategory_id() {
        return category_id;
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

    public List<OptionForm> getOptions() {
        return options;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
