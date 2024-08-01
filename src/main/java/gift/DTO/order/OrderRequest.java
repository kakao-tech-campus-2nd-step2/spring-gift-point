package gift.DTO.order;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderRequest (
  @NotNull(message = "Option id is required")
  @Positive(message = "Option id must be positive")
  Long optionId,

  @NotNull(message = "Order quantity is required")
  @Positive(message = "Order quantity must be positive")
  Long quantity,

  String message
){
}
