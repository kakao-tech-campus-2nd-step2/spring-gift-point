package gift.domain.dto.response;

import gift.domain.entity.Product;

public record WishResponse(
    Long productId,
    String productName,
    Integer productPrice,
    String productImageUrl,
    Long quantity) {

    public static WishResponse of(Long quantity, Product product) {
        return new WishResponse(product.getId(), product.getName(), product.getPrice(), product.getImageUrl(), quantity);
    }
}
