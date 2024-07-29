package gift.service;

import gift.dto.ProductRequest;
import gift.dto.ProductResponse;
import gift.entity.Category;
import gift.entity.Option;
import gift.entity.Product;
import gift.repository.ProductRepository;
import gift.validator.ProductNameValidator;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductNameValidator productNameValidator;
    private final CategoryService categoryService;

    public ProductService(ProductRepository productRepository,
        ProductNameValidator productNameValidator,
        CategoryService categoryService) {
        this.productRepository = productRepository;
        this.productNameValidator = productNameValidator;
        this.categoryService = categoryService;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Slice<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public ProductResponse addProduct(@Valid ProductRequest productRequest) {

        Category category = categoryService.findById(productRequest.getCategoryId())
            .orElseThrow(() -> new IllegalArgumentException("category ID를 찾을 수 없음"));

        Product product = ProductRequest.toEntity(productRequest, category);

        List<Option> options = productRequest.getOptions().stream()
            .map(optionRequest -> new Option(optionRequest.getName(), optionRequest.getQuantity(),
                product))
            .toList();

        options.forEach(product::addOption);

        validateProduct(product);

        Product savedProduct = productRepository.save(product);
        return ProductResponse.from(savedProduct);
    }

    public ProductResponse updateProduct(Long id, @Valid ProductRequest updatedProductRequest) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없음"));
        Category category = categoryService.findById(updatedProductRequest.getCategoryId())
            .orElseThrow(() -> new IllegalArgumentException("category ID를 찾을 수 없음"));

        product.updateProduct(updatedProductRequest.getName(), updatedProductRequest.getPrice(),
            updatedProductRequest.getImgUrl(), category);

        validateProduct(product);

        Product savedProduct = productRepository.save(product);
        return ProductResponse.from(savedProduct);
    }

    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    private void validateProduct(Product product) {
        BindingResult result = new BeanPropertyBindingResult(product, "product");
        productNameValidator.validate(product, result);
        if (result.hasErrors()) {
            throw new IllegalArgumentException(result.getFieldError().getDefaultMessage());
        }
    }

}
