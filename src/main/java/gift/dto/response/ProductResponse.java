package gift.dto.response;

import gift.domain.Product;

public record ProductResponse(Long id, String name, int price, String imageUrl, Long categoryId) {
    public static ProductResponse from(final Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getImageUrl(), product.getCategory().getId());
    }
}
