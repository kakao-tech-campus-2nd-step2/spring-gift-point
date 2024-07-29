package gift.dto.response;

import gift.domain.Wish;

public record WishResponse(Long id, OptionResponse optionResponse, int quantity) {
    public static WishResponse from(final Wish wish){
        return new WishResponse(wish.getId(), OptionResponse.from(wish.getOption()), wish.getQuantity());
    }
}
