package gift.dto.Request;

import java.util.List;

public record WishRequestDto(
    Long productId,
    int count,
    List<OptionRequestDto> options
) {
    public static record OptionRequestDto(
        Long id,
        int quantity
    ) {}
}
