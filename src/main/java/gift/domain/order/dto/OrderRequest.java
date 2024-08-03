package gift.domain.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "주문 요청 DTO")
public class OrderRequest {

    @Schema(description = "옵션 이름", example = "1L")
    private Long optionId;
    @Schema(description = "옵션 이름", example = "10")
    private int quantity;
    @Schema(description = "옵션 이름", example = "배송 메세지")
    private String message;

    @Schema(description = "현금 영수증", example = "010-1234-5678")
    private String phoneNumber;

    private OrderRequest() {
    }

    public OrderRequest(Long optionId, int quantity, String message, String phoneNumber) {
        this.optionId = optionId;
        this.quantity = quantity;
        this.message = message;
        this.phoneNumber = phoneNumber;
    }

    public Long getOptionId() {
        return optionId;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getMessage() {
        return message;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
