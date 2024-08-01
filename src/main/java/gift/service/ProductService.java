package gift.service;

import gift.dto.ProductRequest;
import gift.dto.ProductResponse;
import gift.entity.Category;
import gift.entity.Product;
import gift.repository.ProductRepository;
import gift.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public Page<ProductResponse> findAll(PageRequest pageRequest) {
        return productRepository.findAll(pageRequest).map(this::convertToResponse);
    }

    public Page<ProductResponse> findAllByCategoryId(Long categoryId, PageRequest pageRequest) {
        return productRepository.findByCategoryId(categoryId, pageRequest).map(this::convertToResponse);
    }

    public ProductResponse findById(long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Product with id " + id + " not found"));
        return convertToResponse(product);
    }

    public void save(ProductRequest productRequest) {
        Category category = categoryRepository.findById(productRequest.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));
        Product product = new Product(productRequest.name(), productRequest.price(), productRequest.imageUrl(), category);
        productRepository.save(product);
    }

    public void update(Long id, ProductRequest productRequest) {
        Category category = categoryRepository.findById(productRequest.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));
        Product product = new Product(id, productRequest.name(), productRequest.price(), productRequest.imageUrl(), category);
        productRepository.save(product);
    }

    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    public void deleteBatch(List<Long> ids) {
        productRepository.deleteAllById(ids);
    }

    private ProductResponse convertToResponse(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getImageUrl(), product.getCategory().getId());
    }
}
