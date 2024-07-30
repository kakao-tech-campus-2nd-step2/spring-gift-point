package gift.domain.product.service;

import gift.domain.product.dto.ProductReadAllResponse;
import gift.domain.product.dto.ProductRequest;
import gift.domain.product.dto.ProductResponse;
import gift.domain.product.entity.Category;
import gift.domain.product.entity.Product;
import gift.domain.product.repository.CategoryJpaRepository;
import gift.domain.product.repository.ProductJpaRepository;
import gift.domain.wishlist.repository.WishlistJpaRepository;
import gift.exception.InvalidCategoryInfoException;
import gift.exception.InvalidProductInfoException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductJpaRepository productJpaRepository;
    private final OptionService optionService;
    private final CategoryJpaRepository categoryJpaRepository;
    private final WishlistJpaRepository wishlistJpaRepository;

    public ProductService(
        ProductJpaRepository productJpaRepository,
        OptionService optionService,
        CategoryJpaRepository categoryJpaRepository,
        WishlistJpaRepository wishlistJpaRepository
    ) {
        this.productJpaRepository = productJpaRepository;
        this.optionService = optionService;
        this.categoryJpaRepository = categoryJpaRepository;
        this.wishlistJpaRepository = wishlistJpaRepository;
    }

    @Transactional
    public ProductResponse create(ProductRequest productRequest) {
        Category category = categoryJpaRepository.findById(productRequest.categoryId())
            .orElseThrow(() -> new InvalidCategoryInfoException("error.invalid.category.id"));
        Product product = productRequest.toProduct(category);

        optionService.create(product, productRequest.options());
        Product savedProduct = productJpaRepository.save(product);
        return ProductResponse.from(savedProduct);
    }

    public Page<ProductReadAllResponse> readAll(Pageable pageable) {
        Page<Product> foundProducts = productJpaRepository.findAll(pageable);

        if (foundProducts == null) {
            return Page.empty(pageable);
        }
        return foundProducts.map(ProductReadAllResponse::from);
    }

    public ProductResponse readById(long productId) {
        Product foundProduct = productJpaRepository.findById(productId)
            .orElseThrow(() -> new InvalidProductInfoException("error.invalid.product.id"));
        return ProductResponse.from(foundProduct);
    }

    public ProductResponse update(long productId, ProductRequest productRequest) {
        Product product = productJpaRepository.findById(productId)
            .orElseThrow(() -> new InvalidProductInfoException("error.invalid.product.id"));
        Category category = categoryJpaRepository.findById(productRequest.categoryId())
            .orElseThrow(() -> new InvalidCategoryInfoException("error.invalid.category.id"));

        product.updateInfo(category, productRequest.name(), productRequest.price(), productRequest.imageUrl());
        optionService.update(product, productRequest.options());

        Product savedProduct = productJpaRepository.save(product);
        return ProductResponse.from(savedProduct);
    }

    public void delete(long productId) {
        Product product = productJpaRepository.findById(productId)
            .orElseThrow(() -> new InvalidProductInfoException("error.invalid.product.id"));

        optionService.deleteAllByProductId(productId);
        wishlistJpaRepository.deleteAllByProductId(productId);
        productJpaRepository.delete(product);
    }
}
