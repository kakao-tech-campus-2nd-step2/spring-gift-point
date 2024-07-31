package gift.web.dto.request.wishproduct;

import jakarta.validation.constraints.Min;

public class UpdateWishProductRequest {

    @Min(1)
    private Integer quantity;

    private UpdateWishProductRequest() {
    }

    public UpdateWishProductRequest(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
