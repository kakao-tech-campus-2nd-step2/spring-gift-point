package gift.service;

import gift.common.exception.ProductNotFoundException;
import gift.model.category.Category;
import gift.model.option.OptionRequest;
import gift.model.product.Product;
import gift.model.product.ProductRequest;
import gift.model.product.ProductResponse;
import gift.repository.category.CategoryRepository;
import gift.repository.product.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }


    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
            () -> new ProductNotFoundException("해당 Id의 상품은 존재하지 않습니다.")
        );
        return ProductResponse.from(product);
    }

    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable).map(ProductResponse::from);
    }

    public ProductResponse createProduct(ProductRequest productRequest, OptionRequest optionRequest) {
        Category category = categoryRepository.findById(productRequest.categoryId()).orElseThrow();
        Product product = productRequest.toEntity(category, optionRequest.name(), optionRequest.quantity());
        return ProductResponse.from(productRepository.save(product));
    }

    public ProductResponse updateProduct(Long id, ProductRequest updatedProduct) {
        Product product = productRepository.findById(id).orElseThrow(
            () -> new ProductNotFoundException("해당 Id의 상품은 존재하지 않습니다.")
        );
        Category category = categoryRepository.findById(updatedProduct.categoryId()).orElseThrow();
        product.update(updatedProduct, category);
        return ProductResponse.from(productRepository.save(product));
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}