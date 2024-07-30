package gift.main.dto;

import jakarta.validation.constraints.Min;

public record OptionChangeQuantityRequest(
        @Min(1) int quantity) {

}
