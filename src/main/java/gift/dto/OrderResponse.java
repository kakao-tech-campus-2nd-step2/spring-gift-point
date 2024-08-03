package gift.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Response DTO for Order")
public record OrderResponse(
    @Schema(description = "Order ID", example = "1")
    Long id,
    @Schema(description = "Option ID", example = "1")
    Long optionId,
    @Schema(description = "Quantity ordered", example = "2")
    int quantity,
    @Schema(description = "Order date and time", example = "2024-07-28T17:04:18.834374")
    LocalDateTime localDateTime,
    @Schema(description = "Order message", example = "선물이 도착했습니다.")
    String message) {

}
