package gift.domain.model.dto;

import java.time.LocalDateTime;

public class OrderResponseDto {

    private Long id;
    private Long optionId;
    private Integer quantity;
    private LocalDateTime orderDateTime;
    private String message;

    public OrderResponseDto() {
    }

    public OrderResponseDto(Long id, Long optionId, Integer quantity, LocalDateTime orderDateTime,
        String message) {
        this.id = id;
        this.optionId = optionId;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = message;
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

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public String getMessage() {
        return message;
    }
}
