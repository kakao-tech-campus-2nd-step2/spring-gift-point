package gift.product.application.service;

import gift.category.persistence.CategoryRepository;
import gift.exception.InvalidCategory;
import gift.exception.InvalidProduct;
import gift.exception.NotFoundOption;
import gift.option.persistence.Option;
import gift.option.persistence.OptionRepository;
import gift.product.application.dto.ProductRequestDto;
import gift.product.application.dto.ProductResponseDto;
import gift.product.persistence.Product;
import gift.product.persistence.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final OptionRepository optionRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, OptionRepository optionRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.optionRepository = optionRepository;
    }

    public Page<ProductResponseDto> getAllProducts(Long categoryId, Pageable pageable) {
        Page<Product> productPage = productRepository.findAllByCategoryId(categoryId, pageable);

        return productPage.map(product -> new ProductResponseDto(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getImageUrl(),
            product.getCategory().getId(),
            product.getOptions()));
    }

    public Optional<ProductResponseDto> getProductById(Long id) {
        return productRepository.findById(id)
            .map(product -> new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                product.getCategory().getId(),
                product.getOptions()));
    }

    public ProductResponseDto postProduct(ProductRequestDto productRequestDto) {
        optionRepository.save(productRequestDto.options().get(0));
        Product product = productRepository.saveAndFlush(new Product(
            productRequestDto.name(),
            productRequestDto.price(),
            productRequestDto.imageUrl(),
            categoryRepository.findById(productRequestDto.categoryId()).orElseThrow(),
            productRequestDto.options()
        ));
        return new ProductResponseDto(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getImageUrl(),
            product.getCategory().getId(),
            product.getOptions()
        );
    }

    public ProductResponseDto putProduct(Long id, ProductRequestDto productRequestDto) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new InvalidProduct("유효하지 않은 상품입니다"));

        product.update(productRequestDto.name(), productRequestDto.price(), productRequestDto.imageUrl());
        productRepository.saveAndFlush(product);

        return new ProductResponseDto(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getImageUrl(),
            product.getCategory().getId(),
            product.getOptions()
        );
    }

    public ProductResponseDto putCategory(Long productId, Long categoryId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new InvalidProduct("유효하지 않은 상품입니다"));

        product.changeCategory(categoryRepository.findById(categoryId)
            .orElseThrow(() -> new InvalidCategory("유효하지 않은 카테고리입니다")));

        productRepository.saveAndFlush(product);

        return new ProductResponseDto(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getImageUrl(),
            product.getCategory().getId(),
            product.getOptions()
        );
    }

    public void deleteProductById(Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new InvalidProduct("유효하지 않은 상품입니다"));
        productRepository.deleteById(id);
    }

    public Long findPrdouctOfOption(Long optionId) throws NotFoundOption {
        Option option = optionRepository.findById(optionId)
            .orElseThrow(() -> new NotFoundOption("해당 옵션을 찾을 수 없습니다"));
        return option.getProduct().getId();
    }

}
