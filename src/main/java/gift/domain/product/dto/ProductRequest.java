package gift.domain.product.dto;

import gift.domain.option.dto.OptionRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "상품 요청 DTO")
public class ProductRequest {

    @NotBlank
    @Size(max = 15, message = "최대 15자리까지 입력하실 수 있습니다.")
    @Pattern(regexp = "[\\w\\s\\(\\)\\[\\]\\+\\-\\&\\/가-힣]*", message = "특수 문자는 '(), [], +, -, &, /, _ '만 사용가능 합니다.")
    @Pattern(regexp = "^(?!.*카카오).*$", message = "담당 MD와 협의해 주세요.")
    @Schema(description = "상품 이름", example = "상품1")
    private String name;

    @NotNull
    @Schema(description = "상품 가격", example = "1000")
    private int price;

    @NotNull
    @Schema(description = "상품 이미지 URL", example = "http://example.com/image.jpg")
    private String imageUrl;
    @Schema(description = "카테고리 ID", example = "1")
    private Long categoryId;

    @Valid
    @Schema(description = "옵션 요청")
    private OptionRequest optionRequest;

    private ProductRequest() {
    }

    public ProductRequest(String name, int price, String imageUrl, Long categoryId,
        OptionRequest optionRequest) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
        this.optionRequest = optionRequest;
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

    public Long getCategoryId() {
        return categoryId;
    }

    public OptionRequest getOptionRequest() {
        return optionRequest;
    }
}
