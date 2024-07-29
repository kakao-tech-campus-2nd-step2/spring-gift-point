package gift.service;

import gift.domain.Category;
import gift.domain.Option;
import gift.domain.Product;
import gift.dto.OptionDto;
import gift.dto.ProductDto;
import gift.exception.ErrorCode;
import gift.exception.GiftException;
import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductDto> getProducts(Pageable pageable) {
        return productRepository.findAll(pageable).stream()
                .map(Product::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new GiftException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public ProductDto getProduct(Long productId) {
        return getProductById(productId).toDto();
    }

    public void addProduct(ProductDto dto) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new GiftException(ErrorCode.CATEGORY_NOT_FOUND));

        Product product = new Product(dto.getName(), dto.getPrice(), dto.getImageUrl(), category);
        List<Option> options = dto.getOptions().stream()
                .map(OptionDto::toEntity)
                .toList();

        options.forEach(product::addOption);

        productRepository.save(product);
    }

    public void editProduct(Long productId, ProductDto dto) {
        Product product = getProductById(productId);
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new GiftException(ErrorCode.CATEGORY_NOT_FOUND));

        product.changeName(dto.getName());
        product.changePrice(dto.getPrice());
        product.changeImageUrl(dto.getImageUrl());
        product.changeCategory(category);
    }

    public void removeProduct(Long productId) {
        Product product = getProductById(productId);

        productRepository.deleteById(product.getId());
    }

    @Transactional(readOnly = true)
    public List<OptionDto> getOptions(Long productId) {
        Product product = getProductById(productId);

        return product.getOptions().stream()
                .map(Option::toDto)
                .toList();
    }

    public void addOption(Long productId, OptionDto dto) {
        Product product = getProductById(productId);

        Option option = new Option(dto.getName(), dto.getQuantity());

        product.validateOptionNameUnique(option.getName());
        product.addOption(option);
    }

    public void removeOption(Long productId, Long optionId) {
        Product product = getProductById(productId);

        product.removeOptionById(optionId);
    }

    @Retryable(
            retryFor = {ObjectOptimisticLockingFailureException.class},
            maxAttempts = 1000,
            backoff = @Backoff(100)
    )
    public void subtractOptionQuantity(Long productId, Long optionId, Long quantity) {
        Product product = getProductById(productId);
        Option option = product.getOptionById(optionId);

        option.subtract(quantity);
    }

}
