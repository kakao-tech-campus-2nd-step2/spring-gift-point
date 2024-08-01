package gift.dto.giftorder;

import java.util.List;

public record GiftOrderPageResponse(Integer page, Integer size, Long totalElements, Integer totalPages, List<GiftOrderResponse> content) {
}
