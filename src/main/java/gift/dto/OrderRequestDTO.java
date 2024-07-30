package gift.dto;

import gift.entity.Member;
import gift.entity.Option;
import gift.entity.Orders;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

public class OrderRequestDTO {

    @Valid
    @NotEmpty(message = "Option Id는 필수값입니다.")
    private Long optionId;

    @Valid
    @DecimalMin(value = "1", message = "상품의 개수는 1개 이상이어야 합니다.")
    private int quantity;

    private String message;

    public OrderRequestDTO() {
    }

    public OrderRequestDTO(Long optionId, int quantity, String message) {
        this.optionId = optionId;
        this.quantity = quantity;
        this.message = message;
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

    public Orders toEntity(Option option, Member member) {
        return new Orders(option, quantity, message, member, LocalDateTime.now());
    }
}
