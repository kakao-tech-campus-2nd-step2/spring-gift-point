package gift.service;

import static gift.util.constants.CategoryConstants.CATEGORY_NOT_FOUND;
import static gift.util.constants.ProductConstants.INVALID_PRICE;
import static gift.util.constants.ProductConstants.PRODUCT_NOT_FOUND;

import gift.dto.product.ProductCreateRequest;
import gift.dto.product.ProductResponse;
import gift.dto.product.ProductUpdateRequest;
import gift.exception.product.InvalidProductPriceException;
import gift.exception.product.ProductNotFoundException;
import gift.model.Category;
import gift.model.Option;
import gift.model.Product;
import gift.repository.CategoryRepository;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final OptionRepository optionRepository;

    public ProductService(
        ProductRepository productRepository,
        CategoryRepository categoryRepository,
        OptionRepository optionRepository
    ) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.optionRepository = optionRepository;
    }

    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable).map(this::convertToDTO);
    }

    public ProductResponse getProductById(Long id) {
        return productRepository.findById(id)
            .map(this::convertToDTO)
            .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND + id));
    }

    public ProductResponse addProduct(ProductCreateRequest productCreateRequest) {
        validatePrice(productCreateRequest.price());

        Category category = categoryRepository.findById(productCreateRequest.categoryId())
            .orElseThrow(() -> new ProductNotFoundException(
                CATEGORY_NOT_FOUND + productCreateRequest.categoryId()));

        Product product = convertToEntity(productCreateRequest, category);
        Product savedProduct = productRepository.save(product);

        Option defaultOption = new Option(
            "Default Option",
            1,
            savedProduct
        );
        optionRepository.save(defaultOption);

        return convertToDTO(savedProduct);
    }

    public ProductResponse updateProduct(Long id, ProductUpdateRequest productUpdateRequest) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND + id));
        validatePrice(productUpdateRequest.price());

        Category category = categoryRepository.findById(productUpdateRequest.categoryId())
            .orElseThrow(() -> new ProductNotFoundException(
                CATEGORY_NOT_FOUND + productUpdateRequest.categoryId()));

        product.update(
            productUpdateRequest.name(),
            productUpdateRequest.price(),
            productUpdateRequest.imageUrl(),
            category
        );
        Product updatedProduct = productRepository.save(product);
        return convertToDTO(updatedProduct);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(PRODUCT_NOT_FOUND + id);
        }
        productRepository.deleteById(id);
    }

    private static void validatePrice(int price) {
        if (price < 0) {
            throw new InvalidProductPriceException(INVALID_PRICE);
        }
    }

    // Mapper methods
    private ProductResponse convertToDTO(Product product) {
        return new ProductResponse(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getImageUrl(),
            product.getCategoryId(),
            product.getCategoryName()
        );
    }

    private Product convertToEntity(ProductCreateRequest productCreateRequest, Category category) {
        return new Product(
            productCreateRequest.name(),
            productCreateRequest.price(),
            productCreateRequest.imageUrl(),
            category
        );
    }

    public Product convertToEntity(ProductResponse productResponse) {
        Category category = categoryRepository.findById(productResponse.categoryId())
            .orElseThrow(() -> new ProductNotFoundException(
                CATEGORY_NOT_FOUND + productResponse.categoryId()));

        return new Product(
            productResponse.id(),
            productResponse.name(),
            productResponse.price(),
            productResponse.imageUrl(),
            category
        );
    }
}
