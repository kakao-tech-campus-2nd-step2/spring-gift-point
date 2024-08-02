package gift.product.dto;

import gift.product.model.Member;
import gift.product.model.Option;
import gift.product.model.Order;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class OrderRequestDTO {

    @NotNull(message = "주문할 옵션을 선택해주세요.")
    private Long optionId;
    @Positive(message = "주문 수량은 1이상의 양의 정수여야 합니다.")
    private int quantity;
    private String message;

    public Long getOptionId() {
        return optionId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Order convertToDomain(Option option, Member member) {
        return new Order(
            option,
            quantity,
            message,
            member
        );
    }

}
