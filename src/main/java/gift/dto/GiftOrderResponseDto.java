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
    @Schema(description = "선물 주문 일시")
    private LocalDateTime orderDateTime;
    @Schema(description = "요청 메세지")
    private String message;
    @Schema(description = "할인 전 가격 ")
    private Long originalPrice;
    @Schema(description = "할인 후 가격 ")
    private Long finalPrice;


    public GiftOrderResponseDto(Long id, Long optionId, Long quantity, LocalDateTime orderDateTime, String message, Long originalPrice, Long finalPrice) {
        this.id = id;
        this.optionId = optionId;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = message;
        this.originalPrice = originalPrice;
        this.finalPrice = finalPrice;
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

    public Long getOriginalPrice() {
        return originalPrice;
    }

    public Long getFinalPrice() {
        return finalPrice;
    }
}
