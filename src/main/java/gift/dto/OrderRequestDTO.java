package gift.dto;

import gift.entity.Member;
import gift.entity.Option;
import gift.entity.Orders;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class OrderRequestDTO {

    @Valid
    @NotNull(message = "Option Id는 필수값입니다.")
    private Long optionId;

    @Valid
    @DecimalMin(value = "1", message = "상품의 개수는 1개 이상이어야 합니다.")
    private int quantity;

    @Min(value = 0, message = "사용하려는 포인트는 0보다 크거나 같아야 합니다.")
    private int point;

    private String message;

    public OrderRequestDTO() {
    }

    public OrderRequestDTO(Long optionId, int quantity, int point, String message) {
        this.optionId = optionId;
        this.quantity = quantity;
        this.point = point;
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

    public int getPoint() {
        return point;
    }

    public Orders toEntity(Option option, Member member) {
        return new Orders(option, quantity, message, member, LocalDateTime.now(),point);
    }
}
