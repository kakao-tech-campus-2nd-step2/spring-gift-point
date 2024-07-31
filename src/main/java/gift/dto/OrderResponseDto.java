package gift.dto;

import java.time.LocalDateTime;

public class OrderResponseDto {
    private int id;
    private Long optionId;
    private int quantity;
    private LocalDateTime orderDateTime;
    private String message;

    public OrderResponseDto(int id, Long optionId, int quantity, LocalDateTime orderDateTime, String message) {
        this.id = id;
        this.optionId = optionId;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = message;
    }
}