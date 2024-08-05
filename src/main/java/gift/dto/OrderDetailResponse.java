package gift.dto;

import gift.entity.Option;
import gift.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Response DTO for Order Details")
public record OrderDetailResponse(
    @Schema(description = "Order ID", example = "1")
    Long id,
    @Schema(description = "Order option details")
    Option option,
    @Schema(description = "User details")
    User user,
    @Schema(description = "Quantity ordered", example = "2")
    int quantity,
    @Schema(description = "Order date and time", example = "2024-07-28T17:04:18.834374")
    LocalDateTime localDateTime,
    @Schema(description = "Order message", example = "Please deliver between 9 AM to 5 PM")
    String message) {

}
