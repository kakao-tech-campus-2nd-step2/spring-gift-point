package gift.dto.response;

import gift.domain.Product;

public record ProductResponse(Long id, String name, Integer price, String imageUrl, String categoryName) {


    public static ProductResponse EntityToResponse(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getImageUrl(), product.getCategory().getName());
    }
}
