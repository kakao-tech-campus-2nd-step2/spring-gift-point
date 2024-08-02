package gift.product.dto;

import gift.product.entity.Product;

public record ProductResponseDto(
    Long id,
    String name,
    int price,
    String imageUrl,
    Long categoryId) {


  public static ProductResponseDto toDto(Product product) {
    return new ProductResponseDto(
        product.getId(), product.getName(), product.getPrice(), product.getImageUrl(),
        product.getCategory().getId()
    );
  }

}
