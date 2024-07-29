package gift.dto;

import gift.entity.GiftOrder;

import java.time.LocalDateTime;

public class GiftOrderResponseDto {
    private Long id;
    private Long optionId;
    private Long quantity;
    private LocalDateTime orderDateTime;
    private String message;

    public GiftOrderResponseDto(Long id, Long optionId, Long quantity, LocalDateTime orderDateTime, String message) {
        this.id = id;
        this.optionId = optionId;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = message;
    }

    public GiftOrderResponseDto(Long optionId, Long quantity, LocalDateTime orderDateTime, String message) {
        this.optionId = optionId;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = message;
    }

    public static GiftOrderResponseDto fromEntity(GiftOrder giftOrder) {
        return new GiftOrderResponseDto(giftOrder.getId(), giftOrder.getQuantity(), giftOrder.getOrderDateTime(), giftOrder.getMessage());
    }

    public Long getId() {
        return id;
    }

    public Long getOptionId() {
        return optionId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public String getMessage() {
        return message;
    }
}
