package gift.service;

import gift.common.exception.badRequest.RequestValidationException;
import gift.common.exception.conflict.ProductAlreadyExistsException;
import gift.common.exception.notFound.ProductNotFoundException;
import gift.dto.CategoryResponse;
import gift.dto.OptionRequest;
import gift.dto.ProductRequest;
import gift.dto.ProductResponse;
import gift.dto.ProductResponseWithoutCategoryId;
import gift.dto.ProductUpdateRequest;
import gift.dto.ProductUpdateResponse;
import gift.entity.Category;
import gift.entity.Option;
import gift.entity.Product;
import gift.repository.ProductRepository;
import gift.validator.ProductNameValidator;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
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

    public List<ProductResponseWithoutCategoryId> getProductsByCategory(Long categoryId, String[] sort) {
        Sort sorting = Sort.by(Sort.Order.by(sort[0]).with(Sort.Direction.fromString(sort[1])));
        List<Product> products;
        if (categoryId != null) {
            products = productRepository.findByCategoryId(categoryId, sorting);
        } else {
            products = productRepository.findAll(sorting);
        }
        return products.stream().map(ProductResponseWithoutCategoryId::from).toList();
    }

    public CategoryResponse getCategoryById(Long categoryId) {
        Category category = categoryService.findById(categoryId)
            .orElseThrow(CategoryNotFoundException::new);
        return CategoryResponse.from(category);
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
            .orElseThrow(ProductNotFoundException::new);
    }

    public ProductResponse addProduct(@Valid ProductRequest productRequest) {

        Category category = categoryService.findById(productRequest.getCategoryId())
            .orElseThrow(CategoryNotFoundException::new);

        if (productRepository.findByName(productRequest.getName()).isPresent()) {
            throw new ProductAlreadyExistsException();
        }

        Product product = ProductRequest.toEntity(productRequest, category);

        for (OptionRequest optionRequest : productRequest.getOptions()) {
            Option option = new Option(optionRequest.getName(), optionRequest.getStockQuantity(), product);
            product.addOption(option);
        }

        Product savedProduct = productRepository.save(product);
        return ProductResponse.from(savedProduct);
    }

    public ProductUpdateResponse updateProduct(Long id, @Valid ProductUpdateRequest updatedProductRequest) {
        Product product = findById(id);

        Category category = categoryService.findById(updatedProductRequest.getCategoryId())
            .orElseThrow(CategoryNotFoundException::new);

        Optional<Product> existingProduct = productRepository.findByName(updatedProductRequest.getName());
        if (existingProduct.isPresent() && !existingProduct.get().getId().equals(id)) {
            throw new ProductAlreadyExistsException();
        }

        product.updateProduct(updatedProductRequest.getName(), updatedProductRequest.getPrice(),
            updatedProductRequest.getImgUrl(), category);

        validateProduct(product);

        Product savedProduct = productRepository.save(product);
        return ProductUpdateResponse.from(savedProduct);
    }

    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    private void validateProduct(Product product) {
        BindingResult result = new BeanPropertyBindingResult(product, "product");
        productNameValidator.validate(product, result);
        if (result.hasErrors()) {
            throw new RequestValidationException(result.getFieldError().getDefaultMessage());
        }
    }

}
