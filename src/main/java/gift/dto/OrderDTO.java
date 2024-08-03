package gift.dto;

import gift.entity.OrderEntity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class OrderDTO {
    @NotNull(message = "옵션 id는 필수 입력 사항입니다.")
    private Long optionId;

    @Min(value = 1, message = "수량은 1 이상의 숫자여야 합니다.")
    private int quantity;
    private String message;

    private LocalDateTime orderDateTime;

    // 기본 생성자
    public OrderDTO() {}

    // 파라미터 생성자
    public OrderDTO(Long optionId, int quantity, String message) {
        this.optionId = optionId;
        this.quantity = quantity;
        this.message = message;
    }

    public OrderDTO(Long optionId, int quantity, String message, LocalDateTime orderDateTime) {
        this.optionId = optionId;
        this.quantity = quantity;
        this.message = message;
        this.orderDateTime = orderDateTime;
    }

    // Getters and Setters
    public Long getOptionId() {
        return optionId;
    }

    public void setOptionId(Long optionId) {
        this.optionId = optionId;
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
}


