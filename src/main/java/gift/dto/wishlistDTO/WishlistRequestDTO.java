package gift.dto.wishlistDTO;

import jakarta.validation.constraints.NotNull;

public record WishlistRequestDTO(
    @NotNull(message = "옵션 ID는 필수입니다.")
    Long optionId
) {

}