package gift.web.dto.request.productoption;

public class SubtractProductOptionQuantityRequest {

    private final Integer quantity;

    public SubtractProductOptionQuantityRequest(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
