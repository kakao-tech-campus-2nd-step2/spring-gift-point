package gift.dto.wish;

import jakarta.validation.constraints.NotNull;

public record WishRequestDTO(@NotNull Long optionId) {
}
