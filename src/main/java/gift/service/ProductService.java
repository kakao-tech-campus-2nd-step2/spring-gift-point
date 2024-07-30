package gift.service;

import gift.domain.Category;
import gift.domain.Product;
import gift.dto.request.OptionRequest;
import gift.dto.request.ProductRequest;
import gift.exception.CategoryNotFoundException;
import gift.exception.InvalidProductDataException;
import gift.exception.ProductNotFoundException;
import gift.repository.category.CategorySpringDataJpaRepository;
import gift.repository.product.ProductSpringDataJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static gift.exception.ErrorCode.*;

@Service
@Transactional(readOnly = true)
public class ProductService {
    private final ProductSpringDataJpaRepository productRepository;
    private final CategorySpringDataJpaRepository categoryRepository;
    private final OptionService optionService;

    @Autowired
    public ProductService(ProductSpringDataJpaRepository productRepository, CategorySpringDataJpaRepository categoryRepository, OptionService optionService) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.optionService = optionService;
    }

    @Transactional
    public Product register(ProductRequest productRequest, OptionRequest optionRequest) {
        Category category = categoryRepository.findByName(productRequest.getCategoryName()).
                orElseThrow(() -> new CategoryNotFoundException(CATEGORY_NOT_FOUND));

        Product product = new Product(productRequest, category);

        try {
            Product savedProduct = productRepository.save(product);
            optionService.addOptionToProduct(savedProduct.getId(), optionRequest);
            return savedProduct;
        } catch (DataIntegrityViolationException e) {
            throw new InvalidProductDataException("상품 데이터가 유효하지 않습니다: " + e.getMessage(), e);
        }
    }

    public Page<Product> getProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Product findOne(Long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND));
    }

    @Transactional
    public Product update(Long productId, ProductRequest productRequest) {
        Product product = productRepository.findById(productId).
                orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND));
        Category category = categoryRepository.findByName(productRequest.getCategoryName()).
                orElseThrow(() -> new CategoryNotFoundException(CATEGORY_NOT_FOUND));

        product.update(productRequest, category);

        try {
            return productRepository.save(product);
        } catch (DataIntegrityViolationException e) {
            throw new InvalidProductDataException("상품 데이터가 유효하지 않습니다: " + e.getMessage(), e);
        }
    }

    public Product delete(Long productId) {
        Product product = productRepository.findById(productId).
                orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND));
        productRepository.delete(product);
        return product;
    }

}
