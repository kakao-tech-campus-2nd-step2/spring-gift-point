package gift.dto;

import gift.domain.Product;

public record ProductResponseDto(Long id, String name, int price, String imageUrl) {
    public static ProductResponseDto convertToDto(Product product) {
        return new ProductResponseDto(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getImageUrl()
        );
    }
}
