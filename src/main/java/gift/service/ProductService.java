package gift.service;

import gift.common.exception.conflict.ProductAlreadyExistsException;
import gift.dto.ProductRequest;
import gift.dto.ProductResponse;
import gift.entity.Category;
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
import gift.common.exception.notFound.CategoryNotFoundException;

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

    public Slice<ProductResponse> findAll(Pageable pageable, Long categoryId) {
        Slice<Product> products;
        if (categoryId != null) {
            products = productRepository.findByCategoryId(categoryId, pageable);
        } else {
            products = productRepository.findAll(pageable);
        }
        return products.map(ProductResponse::from);
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public ProductResponse addProduct(@Valid ProductRequest productRequest) {

        Category category = categoryService.findById(productRequest.getCategoryId())
            .orElseThrow(CategoryNotFoundException::new);

        if (productRepository.findByName(productRequest.getName()).isPresent()) {
            throw new ProductAlreadyExistsException();
        }

        Product product = ProductRequest.toEntity(productRequest, category);

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
