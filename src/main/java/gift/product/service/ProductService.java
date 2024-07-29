package gift.product.service;

import gift.product.dto.product.ProductDto;
import gift.product.model.Category;
import gift.product.model.Option;
import gift.product.model.Product;
import gift.product.repository.CategoryRepository;
import gift.product.repository.OptionRepository;
import gift.product.repository.ProductRepository;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    private final OptionRepository optionRepository;

    public ProductService(ProductRepository productRepository,
        CategoryRepository categoryRepository, OptionRepository optionRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.optionRepository = optionRepository;
    }

    public List<Product> getProductAll() {
        return productRepository.findAll();
    }

    public Page<Product> getProductAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Product getProduct(Long id) {
        return getValidatedProduct(id);
    }

    @Transactional
    public Product insertProduct(ProductDto productDto) {
        Category category = getValidatedCategory(productDto.categoryName());
        Product product = new Product(productDto.name(), productDto.price(), productDto.imageUrl(),
            category);
        Product savedProduct = productRepository.save(product);

        optionRepository.save(new Option("기본", 1, savedProduct));

        return savedProduct;
    }

    @Transactional
    public Product updateProduct(Long id, ProductDto productDto) {
        getValidatedProduct(id);
        Category category = getValidatedCategory(productDto.categoryName());

        Product product = new Product(id, productDto.name(), productDto.price(),
            productDto.imageUrl(), category);

        return productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(Long id) {
        getValidatedProduct(id);
        productRepository.deleteById(id);
    }

    private Product getValidatedProduct(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("해당 ID의 상품이 존재하지 않습니다."));
    }

    private Category getValidatedCategory(String categoryName) {
        return categoryRepository.findByName(categoryName)
            .orElseThrow(() -> new NoSuchElementException("해당 카테고리가 존재하지 않습니다."));
    }
}
