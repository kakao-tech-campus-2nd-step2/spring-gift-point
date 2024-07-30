package gift.product.infra;

import gift.product.domain.Product;
import gift.product.domain.ProductOption;
import gift.product.exception.ProductException;
import gift.util.ErrorCode;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepository {
    private final ProductJpaRepository productJpaRepository;
    private final ProductOptionJpaRepository productOptionJpaRepository;

    public ProductRepository(ProductJpaRepository productJpaRepository, ProductOptionJpaRepository productOptionJpaRepository) {
        this.productJpaRepository = productJpaRepository;
        this.productOptionJpaRepository = productOptionJpaRepository;
    }


    public Product save(Product product) {
        return productJpaRepository.save(product);
    }

    public Product findById(Long id) {
        return productJpaRepository.findById(id).orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_NOT_FOUND));
    }


    public void deleteById(Long id) {
        productJpaRepository.deleteById(id);
    }

    public List<Product> findAll() {
        return productJpaRepository.findAll();
    }

    public List<ProductOption> findProductOptionsByProductId(Long productId) {
        return productOptionJpaRepository.findByProductId(productId);
    }

    public ProductOption saveProductOption(ProductOption productOption) {
        return productOptionJpaRepository.save(productOption);
    }

    public ProductOption getProductWithOption(Long productId, Long optionId) {
        return productOptionJpaRepository.findByProductIdAndId(productId, optionId)
                .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_OPTION_NOT_FOUND));
    }
}
