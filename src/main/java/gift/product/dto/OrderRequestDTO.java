package gift.product.dto;

import gift.product.model.Member;
import gift.product.model.Option;
import gift.product.model.Order;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class OrderRequestDTO {

    @NotNull
    private Long optionId;
    @Positive
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
