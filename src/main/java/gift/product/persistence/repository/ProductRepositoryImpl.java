package gift.product.persistence.repository;

import gift.global.exception.ErrorCode;
import gift.global.exception.custrom.NotFoundException;
import gift.product.persistence.entity.Product;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;

    public ProductRepositoryImpl(ProductJpaRepository productJpaRepository) {
        this.productJpaRepository = productJpaRepository;
    }

    @Override
    public Product getProductById(Long id) {
        return productJpaRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(
                ErrorCode.DB_NOT_FOUND,
                "Product with id " + id + " not found")
            );
    }

    @Override
    public Long saveProduct(Product product) {
        return productJpaRepository.save(product).getId();
    }

    @Override
    public Long deleteProductById(Long id) {
        if (!productJpaRepository.existsById(id)) {
            throw new NotFoundException(
                ErrorCode.DB_NOT_FOUND,
                "Product with id " + id + " not found"
            );
        }
        productJpaRepository.deleteById(id);
        return id;
    }

    @Override
    public List<Product> getAllProducts() {
        return productJpaRepository.findAll();
    }

    @Override
    public void deleteProductByIdList(List<Long> productIds) {
        productJpaRepository.deleteAllById(productIds);
    }

    @Override
    public Map<Long, Product> getProductsByIds(List<Long> productIds) {
        return productJpaRepository.findAllById(productIds)
            .stream()
            .collect(Collectors.toMap(
                Product::getId,
                product -> product
            ));
    }

    @Override
    public Product getReferencedProduct(Long productId) {
        return productJpaRepository.getReferenceById(productId);
    }

    @Override
    public Page<Product> getProductsByPage(Pageable pageRequest) {
        return productJpaRepository.findAll(pageRequest);
    }
}
