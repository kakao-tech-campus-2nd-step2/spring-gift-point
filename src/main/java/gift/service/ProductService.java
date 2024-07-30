package gift.service;

import gift.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface ProductService {
    Page<Product> getProducts(Pageable pageable, Long categoryId);
    Product getProductById(Long id);
    boolean createProduct(Product product);
    boolean updateProduct(Long id, Product product);
    boolean patchProduct(Long id, Map<String, Object> updates);
    List<Product> patchProducts(List<Map<String, Object>> updatesList);
    boolean deleteProduct(Long id);
}
