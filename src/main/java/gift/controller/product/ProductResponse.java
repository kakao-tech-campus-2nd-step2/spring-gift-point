package gift.controller.product;

import java.util.UUID;

public record ProductResponse(UUID id, String name, Long price, String imageUrl, UUID categoryId) {

}