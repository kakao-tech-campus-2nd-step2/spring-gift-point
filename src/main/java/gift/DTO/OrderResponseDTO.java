package gift.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

public class OrderResponseDTO {
    @Schema(description = "주문한 상품의 ID", defaultValue = "1")
    private Long id;
    @Schema(description = "주문한 옵션의 ID", defaultValue = "1")
    private Long optionId;
    @Schema(description = "주문한 상품의 수량", defaultValue = "1")
    private int quantity;
    @Schema(description = "주문한 시간", defaultValue = "2024-01-01T00:00:00")
    private String orderDateTime;
    @Schema(description = "전달된 메세지", defaultValue = "전달된 메세지")
    private String message;

    public Long getId() {
        return id;
    }

    public Long getOptionId() {
        return optionId;
    }

    public int getQuantity() {
        return quantity;
    }


    public String getOrderDateTime() {
        return orderDateTime;
    }

    public String getMessage() {
        return message;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOptionId(Long optionId) {
        this.optionId = optionId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setOrderDateTime(String orderDateTime) {
        this.orderDateTime = orderDateTime;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
