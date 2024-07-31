package gift.domain.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class WishAddRequestDto {

    @NotNull
    private final Long productId;

    @NotNull
    @Min(value = 1)
    @Max(value = 100)
    private final Integer count;

    public WishAddRequestDto(Long productId, Integer count) {
        this.productId = productId;
        this.count = count;
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getCount() {
        return count;
    }
}
