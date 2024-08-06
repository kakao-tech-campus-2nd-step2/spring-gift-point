package gift.controller.product;

import java.util.UUID;

public record WishProductResponse(UUID id, String name, Long price, String imageUrl, String categoryName) {

}