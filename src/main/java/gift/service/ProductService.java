package gift.service;

import gift.domain.Category;
import gift.domain.Product;
import gift.dto.request.OptionRequest;
import gift.dto.request.ProductRequest;
import gift.dto.response.ProductPageResponse;
import gift.dto.response.ProductResponse;
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

import java.util.List;
import java.util.stream.Collectors;

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
    public ProductResponse register(ProductRequest productRequest, OptionRequest optionRequest) {
        Category category = categoryRepository.findByName(productRequest.getCategoryName()).
                orElseThrow(() -> new CategoryNotFoundException(CATEGORY_NOT_FOUND));

        Product product = new Product(productRequest, category);

        try {
            Product savedProduct = productRepository.save(product);
            optionService.addOptionToProduct(savedProduct.getId(), optionRequest);
            return ProductResponse.fromProduct(savedProduct);
        } catch (DataIntegrityViolationException e) {
            throw new InvalidProductDataException("상품 데이터가 유효하지 않습니다: " + e.getMessage(), e);
        }
    }

    public ProductPageResponse getProducts(Long categoryId, Pageable pageable) {
        Page<Product> products;
        if (categoryId != null) {
            products = productRepository.findByCategoryId(categoryId, pageable);
        } else {
            products = productRepository.findAll(pageable);
        }

        return ProductPageResponse.fromProductPage(products);
    }

    public ProductResponse findOne(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND));
        return ProductResponse.fromProduct(product);
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
