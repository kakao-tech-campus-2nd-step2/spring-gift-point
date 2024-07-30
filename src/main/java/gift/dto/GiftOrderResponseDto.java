package gift.dto;

import gift.entity.GiftOrder;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "선물 주문 응답 DTO")
public class GiftOrderResponseDto {
    @Schema(description = "선물 주문 고유 id ")
    private Long id;
    @Schema(description = "선택한 옵션 id")
    private Long optionId;
    @Schema(description = "선택한 상품 개수")
    private Long quantity;
    @Schema(description = "요청 메세지")
    private String message;
    @Schema(description = "선물 주문 일시")
    private LocalDateTime orderDateTime;

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
