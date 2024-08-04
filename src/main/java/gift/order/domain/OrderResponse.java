package gift.order.domain;

import org.springframework.stereotype.Component;

public record OrderResponse(Long id, Long optionId, Long quantity, String orderDateTime, String message) {

}
