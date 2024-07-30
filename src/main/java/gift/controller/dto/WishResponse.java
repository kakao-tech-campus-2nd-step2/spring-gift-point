package gift.controller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class WishResponse {
    private Long id;
    @NotNull
    private Long productId;
    @Min(1)
    @NotNull
    private int count;

    public WishResponse(Long id, Long productId, int count) {
        this.id = id;
        this.productId = productId;
        this.count = count;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
