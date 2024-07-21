package gift.product.service;

import static gift.util.Utils.DEFAULT_CATEGORY_ID;
import static gift.util.Utils.TUPLE_PRODUCT_KEY;
import static gift.util.Utils.TUPLE_WISH_COUNT_KEY;

import gift.category.model.dto.Category;
import gift.category.service.CategoryService;
import gift.product.model.ProductRepository;
import gift.product.model.dto.option.OptionResponse;
import gift.product.model.dto.product.CreateProductRequest;
import gift.product.model.dto.product.Product;
import gift.product.model.dto.product.ProductResponse;
import gift.product.model.dto.product.UpdateProductRequest;
import gift.user.exception.ForbiddenException;
import gift.user.model.dto.AppUser;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Tuple;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final OptionService optionService;

    public ProductService(ProductRepository productRepository, CategoryService categoryService,
                          OptionService optionService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.optionService = optionService;
    }

    @Transactional(readOnly = true)
    public Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product"));
    }

    @Transactional(readOnly = true)
    public ProductResponse findProductWithWishCount(Long id) {
        Optional<Tuple> result = productRepository.findProductByIdWithWishCount(id);
        return result.map(this::mapTupleToProductResponse)
                .orElseThrow(() -> new EntityNotFoundException("Product"));
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> findAllProductWithWishCountPageable(Pageable pageable) {
        Page<Tuple> results = productRepository.findAllActiveProductsWithWishCountPageable(pageable);
        return results.map(this::mapTupleToProductResponse);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> findActiveProductsByCategoryWithWishCount(Long categoryId, Pageable pageable) {
        Page<Tuple> results = productRepository.findActiveProductsByCategoryWithWishCount(categoryId, pageable);
        return results.map(this::mapTupleToProductResponse);
    }

    @Transactional
    public void addProduct(AppUser appUser, CreateProductRequest createProductRequest) {
        Category category = categoryService.getCategory(DEFAULT_CATEGORY_ID);

        Product product = new Product(createProductRequest.name(), createProductRequest.price(),
                createProductRequest.imageUrl(), appUser, category);

        optionService.addOptionList(product, createProductRequest.options());
        productRepository.save(product);
    }

    @Transactional
    public void updateProduct(AppUser appUser, Long id, UpdateProductRequest updateProductRequest) {
        Product product = findProduct(id);
        checkProductOwner(appUser, product);

        product.updateProduct(updateProductRequest.name(), updateProductRequest.price(),
                updateProductRequest.imageUrl());
        productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(AppUser appUser, Long id) {
        Product product = findProduct(id);
        checkProductOwner(appUser, product);

        productRepository.delete(product);
    }

    private void checkProductOwner(AppUser appUser, Product product) {
        if (product.isOwner(appUser.getId()) || appUser.isAdmin()) {
            return;
        }
        throw new ForbiddenException("해당 상품에 대한 권한이 없습니다.");
    }

    private ProductResponse mapTupleToProductResponse(Tuple tuple) {
        Product product = tuple.get(TUPLE_PRODUCT_KEY, Product.class);
        Long wishCount = tuple.get(TUPLE_WISH_COUNT_KEY, Long.class);
        List<OptionResponse> options = optionService.findOptionsByProductId(product.getId());
        return new ProductResponse(product, options, wishCount);
    }
}