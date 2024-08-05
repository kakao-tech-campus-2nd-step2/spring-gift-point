package gift.product.infra;

import gift.product.domain.CreateProductOptionRequestDTO;
import gift.product.domain.Product;
import gift.product.domain.ProductOption;
import gift.product.exception.ProductException;
import gift.util.ErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public Page<Product> findAll(Pageable pageable) {
        return productJpaRepository.findAll(pageable);
    }


    public List<ProductOption> findProductOptionsByProductId(Long productId) {
        return productOptionJpaRepository.findByProductId(productId);
    }

    public ProductOption getProductWithOption(Long productId, Long optionId) {
        return productOptionJpaRepository.findByProductIdAndId(productId, optionId)
                .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_OPTION_NOT_FOUND));
    }

    public Product updateProductOption(Product product, CreateProductOptionRequestDTO createProductOptionRequestDTO) {
        productOptionJpaRepository.save(new ProductOption(createProductOptionRequestDTO.getName(), createProductOptionRequestDTO.getQuantity(), product));
        return productJpaRepository.save(product);
    }

    public Page<Product> findByCategoryId(Long categoryId, Pageable pageable) {
        return productJpaRepository.findByCategoryId(categoryId, pageable);
    }

    public void decreaseOptionQuantity(Long optionId, Long quantity) {
        ProductOption productOption = productOptionJpaRepository.findById(optionId)
                .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_OPTION_NOT_FOUND));
        productOption.decreaseQuantity(quantity);
        productOptionJpaRepository.save(productOption);
    }


    public Map<Long, Product> getProductsByIds(List<Long> productIds) {
        List<Product> products = productJpaRepository.findByIdIn(productIds);
        return products.stream()
                .collect(Collectors.toMap(Product::getId, product -> product));
    }
}
