package gift.domain.dto.response;

import gift.domain.entity.Wish;

public record WishResponse(Long id, ProductCoreInfoResponse product) {

    public static WishResponse of(Wish wish) {
        return new WishResponse(wish.getId(), ProductCoreInfoResponse.of(wish.getProduct()));
    }
}
