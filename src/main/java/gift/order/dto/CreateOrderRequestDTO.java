package gift.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "주문 수신 DTO")
public class CreateOrderRequestDTO {

    @Schema(description = "구매하고자 하는 옵션의 id")
    private long optionId;

    @Schema(description = "구매하고자 하는 수량")
    private int quantity;

    @Schema(description = "보내고 싶은 메시지")
    private String message;

    @Schema(description = "현금 영수증 제출 용 휴대전화 번호")
    private String phoneNumber;

    public long getOptionId() {
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
