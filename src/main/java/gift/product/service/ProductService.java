package gift.product.service;

import static gift.util.Utils.DEFAULT_CATEGORY_ID;
import static gift.util.Utils.TUPLE_PRODUCT_KEY;
import static gift.util.Utils.TUPLE_WISH_COUNT_KEY;

import gift.category.model.dto.Category;
import gift.category.service.CategoryService;
import gift.product.model.ProductRepository;
import gift.product.model.dto.CreateProductRequest;
import gift.product.model.dto.Product;
import gift.product.model.dto.ProductResponse;
import gift.product.model.dto.UpdateProductRequest;
import gift.user.exception.ForbiddenException;
import gift.user.model.dto.AppUser;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Tuple;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    public ProductService(ProductRepository productRepository, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
    }

    @Transactional(readOnly = true)
    public Product findProduct(Long id) {
        return productRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Product"));
    }

    @Transactional(readOnly = true)
    public ProductResponse findProductWithWishCount(Long id) {
        Optional<Tuple> result = productRepository.findProductByIdWithWishCount(id);
        return result.map(
                        tuple -> new ProductResponse(tuple.get(TUPLE_PRODUCT_KEY, Product.class),
                                tuple.get(TUPLE_WISH_COUNT_KEY, Long.class)))
                .orElseThrow(() -> new EntityNotFoundException("Product"));
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> findAllProductWithWishCountPageable(Pageable pageable) {
        Page<Tuple> results = productRepository.findAllActiveProductsWithWishCountPageable(pageable);
        return results.map(tuple -> new ProductResponse(
                tuple.get(TUPLE_PRODUCT_KEY, Product.class),
                tuple.get(TUPLE_WISH_COUNT_KEY, Long.class))
        );
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> findActiveProductsByCategoryWithWishCount(Long categoryId, Pageable pageable) {
        Page<Tuple> results = productRepository.findActiveProductsByCategoryWithWishCount(categoryId, pageable);
        return results.map(tuple -> new ProductResponse(
                tuple.get(TUPLE_PRODUCT_KEY, Product.class),
                tuple.get(TUPLE_WISH_COUNT_KEY, Long.class))
        );
    }

    @Transactional
    public void addProduct(AppUser appUser, CreateProductRequest createProductRequest) {
        Category category = categoryService.getCategory(DEFAULT_CATEGORY_ID);
        Product product = new Product(createProductRequest.name(), createProductRequest.price(),
                createProductRequest.imageUrl(), appUser, category);
        productRepository.save(product);
    }

    @Transactional
    public void updateProduct(AppUser appUser, Long id, UpdateProductRequest updateProductRequest) {
        Product product = findProduct(id);
        checkProductOwner(appUser, product);

        product.setName(updateProductRequest.name());
        product.setPrice(updateProductRequest.price());
        product.setImageUrl(updateProductRequest.imageUrl());
        productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(AppUser appUser, Long id) {
        Product product = findProduct(id);
        checkProductOwner(appUser, product);

        product.setActive(false);
        productRepository.save(product);
    }

    private void checkProductOwner(AppUser appUser, Product product) {
        if (product.isOwner(appUser.getId()) || appUser.isAdmin()) {
            return;
        }
        throw new ForbiddenException("해당 상품에 대한 권한이 없습니다.");
    }
}