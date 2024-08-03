package gift.web.dto.wish;

import jakarta.validation.constraints.NotNull;

public record WishRequestDto(
    @NotNull
    Long productId
    ) { }
