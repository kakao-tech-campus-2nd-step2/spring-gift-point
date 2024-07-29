package gift.order.domain;

import org.springframework.stereotype.Component;

public record OrderResponse(Long id, Long optionid, Long quantity, String orderDateTime, String message) {

}
