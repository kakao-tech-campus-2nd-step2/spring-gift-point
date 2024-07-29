package gift.dto;

import gift.entity.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request DTO for creating an Order")
public class OrderRequest {

    @NotNull
    @Schema(description = "ID of the option", example = "1")
    private Long optionId;
    @NotNull
    @Schema(description = "Quantity of the option", example = "2")
    private Integer quantity;

    @Schema(description = "Message for the order", example = "Please deliver between 9 AM to 5 PM")
    private String message;

    public Long getOptionId() {
        return optionId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getMessage() {
        return message;
    }

    public Integer getTotalPrice(Product product) {
        return product.getPrice() * this.quantity;
    }
}
