package gift.product.dto;

import java.time.LocalDateTime;

public record OptionResponseDto(
    Long id,
    int quantity,
    String name) {

}
