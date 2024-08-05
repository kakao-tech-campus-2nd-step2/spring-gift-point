package gift.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request DTO for updating option quantity")
public record OptionQuantityRequest(
    @Schema(description = "Quantity of the option", example = "10") int quantity) {

}
