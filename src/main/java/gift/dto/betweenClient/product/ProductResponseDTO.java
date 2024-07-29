package gift.dto.betweenClient.product;


import gift.dto.betweenClient.category.CategoryDTO;
import gift.entity.Product;
import gift.exception.BadRequestExceptions.BadRequestException;

public record ProductResponseDTO(
        Long id, String name, Integer price, String imageUrl, CategoryDTO categoryDTO) {

    public static ProductResponseDTO convertToProductResponseDTO(Product product) throws BadRequestException {
        return new ProductResponseDTO(product.getId(), product.getName(), product.getPrice(),
                product.getImageUrl(), CategoryDTO.convertToCategoryDTO(product.getCategory()));
    }
}
