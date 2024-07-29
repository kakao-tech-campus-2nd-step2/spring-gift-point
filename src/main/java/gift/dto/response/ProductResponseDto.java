package gift.dto.response;

import gift.domain.Product;

public record ProductResponseDto(Long id, String name, int price, String imageUrl, CategoryResponseDto categoryResponseDto) {

    public static ProductResponseDto of(Long id, String name, int price, String imageUrl, CategoryResponseDto categoryResponseDto) {
        return new ProductResponseDto(id, name, price, imageUrl, categoryResponseDto);
    }

    public static ProductResponseDto from(Product product) {
        return new ProductResponseDto(product.getId(), product.getName(), product.getPrice(), product.getImageUrl(), CategoryResponseDto.from(product.getCategory()));
    }

}
