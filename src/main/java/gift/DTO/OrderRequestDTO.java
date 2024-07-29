package gift.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

public class OrderRequestDTO {
    @Schema(description = "주문할 옵션 id", defaultValue = "1")
    private Long optionId;
    @Schema(description = "주문할 수량", defaultValue = "1")
    private int quantity;
    @Schema(description = "메세지", defaultValue = "전달 할 메세지")
    private String message;

    public Long getOptionId() {
        return optionId;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getMessage() {
        return message;
    }

    public void setOptionId(Long optionId) {
        this.optionId = optionId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
