package gift.service;

import gift.exception.category.NotFoundCategoryException;
import gift.exception.product.NotFoundProductException;
import gift.model.Product;
import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import gift.response.ProductResponse;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository,
        CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
            .map(ProductResponse::createProductResponse)
            .toList();
    }

    public List<ProductResponse> getPagedAllProducts(Pageable pageable) {
        return productRepository.findPageBy(pageable)
            .getContent()
            .stream()
            .map(ProductResponse::createProductResponse)
            .toList();
    }

    public Product getProduct(Long id) {
        return productRepository.findById(id)
            .orElseThrow(NotFoundProductException::new);
    }

    @Transactional
    public Product addProduct(String name, Integer price, String imageUrl, String categoryName) {
        return categoryRepository.findByName(categoryName)
            .map(
                category -> productRepository.save(new Product(name, price, imageUrl, category))
            ).orElseThrow(NotFoundCategoryException::new);
    }

    @Transactional
    public Product updateProduct(Long id, String name, Integer price, String imageUrl,
        String categoryName) {
        return productRepository.findById(id)
            .map(product -> {
                if (product.getCategory() == null) {
                    throw new NotFoundCategoryException();
                }
                product.updateProduct(name, price, imageUrl, categoryName);
                return product;
            })
            .orElseThrow(NotFoundProductException::new);
    }

    @Transactional
    public void deleteProduct(Long id) {
        productRepository.findById(id)
            .map(product -> {
                productRepository.delete(product);
                return product.getId();
            })
            .orElseThrow(NotFoundProductException::new);
    }
}
