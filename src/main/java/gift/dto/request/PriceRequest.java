package gift.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class PriceRequest {

    @NotNull(message = "옵션 ID를 입력하세요.")
    private Long optionId;

    @NotNull(message = "옵션 수량을 입력하세요.")
    @Min(value = 1, message = "옵션 수량은 최소 1개 이상이어야 합니다.")
    @Max(value = 99999999, message = "옵션 수량은 최대 1억 개 미만이어야 합니다.")
    private Integer quantity;

    @NotNull(message = "상품 ID를 입력하세요.")
    private Long productId;

    public Long getOptionId() {
        return optionId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Long getProductId() {
        return productId;
    }
}
