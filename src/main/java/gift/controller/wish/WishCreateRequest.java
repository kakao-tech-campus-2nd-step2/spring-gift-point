package gift.controller.wish;

import java.util.UUID;

public record WishCreateRequest(UUID productId, Long count) {

}