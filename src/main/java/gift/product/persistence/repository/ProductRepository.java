package gift.product.persistence.repository;

import gift.product.persistence.entity.Product;

import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepository {

    Product getProductById(Long id);

    Long saveProduct(Product product);

    Long deleteProductById(Long id);

    List<Product> getAllProducts();

    void deleteProductByIdList(List<Long> productIds);

    Map<Long, Product> getProductsByIds(List<Long> productIds);

    Product getReferencedProduct(Long productId);

    Page<Product> getProductsByPage(Pageable pageRequest);
}
