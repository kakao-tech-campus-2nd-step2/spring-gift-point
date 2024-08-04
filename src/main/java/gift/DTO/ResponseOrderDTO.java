package gift.DTO;

import gift.Model.Entity.Order;
import gift.Model.Value.CashReceipt;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "주문 응답 DTO")
public class ResponseOrderDTO {
    @Schema(description = "주문 Id")
    private Long id;

    @Schema(description = "옵션 Id")
    private Long optionId;

    @Schema(description = "주문 수량")
    private Integer quantity;

    @Schema(description = "주문 시간")
    private String orderDateTime;

    @Schema(description = "주문 메세지")
    private String message;

    @Schema(description = "주문 메세지")
    private String phoneNumber;

    public ResponseOrderDTO(Long id, Long optionId, Integer quantity, String orderDateTime, String message, String phoneNumber) {
        this.id = id;
        this.optionId = optionId;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = message;
        this.phoneNumber = phoneNumber;
    }

    public Long getId() {
        return id;
    }

    public Long getOptionId() {
        return optionId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getOrderDateTime() {
        return orderDateTime;
    }

    public String getMessage() {
        return message;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public static ResponseOrderDTO of(Order order){
        return new ResponseOrderDTO(
                order.getId(),
                order.getOption().getId(),
                order.getQuantity().getValue(),
                order.getOrderDateTime().toString(),
                order.getMessage().orElseGet(()->""),
                order.getCashReceipt().map(CashReceipt::getValue).orElse(""));
    }

}
