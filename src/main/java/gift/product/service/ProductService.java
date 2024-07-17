package gift.product.service;

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

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
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
                        tuple -> new ProductResponse(tuple.get("product", Product.class), tuple.get("wishCount", Long.class)))
                .orElseThrow(() -> new EntityNotFoundException("Product"));
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> findAllProductWithWishCountPageable(Pageable pageable) {
        Page<Tuple> results = productRepository.findAllActiveProductsWithWishCountPageable(pageable);
        return results.map(tuple -> new ProductResponse(
                tuple.get("product", Product.class),
                tuple.get("wishCount", Long.class))
        );
    }

    @Transactional
    public void addProduct(AppUser appUser, CreateProductRequest createProductRequest) {
        Product product = new Product(createProductRequest.name(), createProductRequest.price(),
                createProductRequest.imageUrl(), appUser);
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