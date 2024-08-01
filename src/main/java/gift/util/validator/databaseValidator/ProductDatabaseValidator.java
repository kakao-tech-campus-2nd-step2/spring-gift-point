package gift.util.validator.databaseValidator;

import gift.dto.betweenClient.product.ProductResponseDTO;
import gift.dto.betweenClient.product.ProductRequestDTO;
import gift.entity.Product;
import gift.exception.BadRequestExceptions.BadRequestException;
import gift.exception.BadRequestExceptions.NoSuchProductIdException;
import gift.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductDatabaseValidator {
    private final ProductRepository productRepository;

    @Autowired
    public ProductDatabaseValidator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product validateProduct(ProductRequestDTO productRequestDTO) {
        return productRepository.findByIdAndNameAndPriceAndImageUrlAndCategoryName(
                productRequestDTO.id(), productRequestDTO.name(), productRequestDTO.price(),
                productRequestDTO.imageUrl(), productRequestDTO.categoryName())
                .orElseThrow(() -> new BadRequestException("그러한 제품은 없습니다."));
    }

    public Product validateProduct(ProductResponseDTO productResponseDTO) {
        return productRepository.findByIdAndNameAndPriceAndImageUrlAndCategory(
                        productResponseDTO.id(), productResponseDTO.name(), productResponseDTO.price(),
                        productResponseDTO.imageUrl(), productResponseDTO.categoryDTO().convertToCategory())
                .orElseThrow(() -> new BadRequestException("그러한 제품은 없습니다."));
    }

    public Product validateProduct(Long productId) {
        return productRepository.findById(productId).orElseThrow(
                () -> new NoSuchProductIdException("id가 %d인 제품은 존재하지 않습니다.".formatted(productId)));
    }

}