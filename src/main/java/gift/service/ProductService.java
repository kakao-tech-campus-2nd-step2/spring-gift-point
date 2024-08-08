package gift.service;

import gift.domain.product.Product;
import gift.domain.product.ProductRequest;
import gift.repository.ProductRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public List<Product> findAll(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return productRepository.findAll(pageable).getContent();
    }

    public Product findById(Long id) {
        return productRepository.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 상품을 찾을 수 없습니다"));
    }

    @Transactional
    public Product save(ProductRequest productRequest) {
        return productRepository.save(productRequest.toProduct());
    }

    @Transactional
    public Product update(Long id, ProductRequest productRequest) {
        Product product = productRepository.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 상품을 찾을 수 없습니다"));
        product.update(productRequest);
        return productRepository.save(product);
    }

    @Transactional
    public void delete(Long id) {
        productRepository.deleteById(id);
    }
}
