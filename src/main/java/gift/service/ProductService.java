package gift.service;

import gift.dto.ProductResponseDto;
import gift.entity.Product;
import gift.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.List;

@ControllerAdvice
@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponseDto createProductDto(String name, Long price, String url) {
        var newProduct = productRepository.save(new Product(name, price, url));
        return new ProductResponseDto(newProduct.getId(), newProduct.getName(), newProduct.getPrice(), newProduct.getUrl());
    }

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public List<ProductResponseDto> getAllAndMakeProductResponseDto() {
        return getAll().stream().map(this::fromEntity).toList();
    }

    public ProductResponseDto getProductResponseDtoById(Long id) {
        Product newProduct = productRepository.findById(id).get();
        return new ProductResponseDto(newProduct.getId(), newProduct.getName(), newProduct.getPrice(), newProduct.getUrl());
    }


    public boolean update(Long id, String name, Long price, String url) {
        Product actualProduct = productRepository.findById(id).orElseThrow(() -> new RuntimeException("상품을 찾지 못했습니다."));
        Product newProduct = new Product(id, name, price, url, actualProduct.getCategory(), actualProduct.getOptions());
        productRepository.save(newProduct);
        return true;
    }

    public void delete(Long id) {
        if (productRepository.findById(id).isPresent()) {
            Product product = productRepository.findById(id).get();
            productRepository.delete(product);
        }
    }

    public ProductResponseDto findProductByName(String name) {
        Product product = productRepository.findByName(name);
        return this.fromEntity(product);
    }

    public Page<ProductResponseDto> getProducts(Pageable pageable) {
        Page<Product> newProduct = productRepository.findAll(pageable);
        return newProduct.map(this::fromEntity);
    }

    public ProductResponseDto fromEntity(Product product) {
        return new ProductResponseDto(product.getName(), product.getPrice(), product.getUrl());
    }

    public Product findById(Long productId) {
        return productRepository.findById(productId).get();
    }
}
