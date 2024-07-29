package gift.service;

import gift.dto.ProductCreateRequest;
import gift.dto.ProductRequest;
import gift.entity.Category;
import gift.entity.Option;
import gift.entity.Product;
import gift.exception.CategoryNotFoundException;
import gift.exception.ProductNotFoundException;
import gift.repository.CategoryRepository;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
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

    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException("해당 id를 가지고있는 Product 객체가 없습니다."));
    }

    @Transactional
    public Product saveProduct(ProductCreateRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
            .orElseThrow(() -> new CategoryNotFoundException("category id에 해당하는 카테고리가 없습니다."));

        Product product = productRepository.save(new Product(request.getName(), request.getPrice(),
            request.getImg(), category));

        request.getOptions().forEach(optionRequest -> {
            Option option = new Option(optionRequest.getName(), optionRequest.getQuantity(),
                product);
            optionRepository.save(option);
            product.addOption(option);
        });

        return product;
    }

    @Transactional
    public Product updateProduct(Long id, ProductRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
            .orElseThrow(() -> new CategoryNotFoundException("category id에 해당하는 카테고리가 없습니다."));
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException("해당 id를 가지고있는 Product 객체가 없습니다."));
        product.updateProduct(request.getName(), request.getPrice(), request.getImg(), category);
        return product;
    }

    public void deleteProduct(Long id) {
        productRepository.findById(id)
            .ifPresentOrElse(product -> productRepository.deleteById(id),
                () -> {
                    throw new ProductNotFoundException("해당 id를 가지고있는 Product 객체가 없습니다.");
                }
            );

    }
}
