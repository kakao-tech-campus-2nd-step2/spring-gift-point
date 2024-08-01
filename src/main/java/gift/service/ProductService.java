package gift.service;
import gift.exception.InvalidProductException;
import gift.exception.ProductNotFoundException;
import gift.repository.ProductRepository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import gift.model.Product;
import java.util.List;


@Service
public class ProductService  {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }


    public Optional<Product> getProductById(Long id) {
        try {
            return productRepository.findById(id);
        } catch (Exception e) {
            throw new IllegalArgumentException("Product not found: " + e.getMessage());
        }
    }


    public Product addProduct(Product product) {
        validateProduct(product);
        return productRepository.save(product);
    }


    public void updateProduct(Long id, Product product) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Product not found");
        }
        product.setId(id);
        validateProduct(product);
        productRepository.save(product);
    }


    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public void validateProduct(Product product) {
        if (product.getName().length() > 15 || product.getName().trim().isEmpty()) {
            throw new InvalidProductException("상품 이름은 공백을 포함하여 최대 15자까지 입력할 수 있습니다.");
        }
        if("카카오".contains(product.getName())) {
            throw new InvalidProductException("\"카카오\"가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있습니다.");
        }
        if (!product.getName().matches("^[\\w\\s\\(\\)\\[\\]\\+\\-\\&\\/\\_가-힣]+$")) {
            throw new InvalidProductException("( ), [ ], +, -, &, /, _ 외 특수 문자는 사용이 불가합니다.");
        }

    }
}