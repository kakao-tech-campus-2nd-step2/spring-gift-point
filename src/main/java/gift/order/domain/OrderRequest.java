package gift.order.domain;

import org.springframework.stereotype.Component;

public record OrderRequest(Long optionId, Long quantity, String message, Long productId) {
}
