package gift.product.service;

import gift.product.domain.Category;
import gift.product.domain.Product;
import gift.product.exception.product.ProductNotFoundException;
import gift.product.persistence.CategoryRepository;
import gift.product.persistence.ProductRepository;
import gift.product.service.command.ProductCommand;
import gift.product.service.dto.ProductInfo;
import gift.product.service.dto.ProductPageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository
    ) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public Long saveProduct(ProductCommand productRequest) {
        Category category = categoryRepository.findById(productRequest.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));
        Product product = productRequest.toEntity(category);
        var newProduct = productRepository.save(product);

        return newProduct.getId();
    }

    @Transactional
    public void modifyProduct(final Long id, ProductCommand productRequest) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> ProductNotFoundException.of(id));
        var category = categoryRepository.findById(productRequest.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));

        product.modify(productRequest.name(), productRequest.price(), productRequest.imgUrl(), category);
    }

    @Transactional(readOnly = true)
    public ProductInfo getProductDetails(final Long id) {
        Product foundProduct = productRepository.findByIdWithCategory(id)
                .orElseThrow(() -> ProductNotFoundException.of(id));

        return ProductInfo.from(foundProduct);
    }

    @Transactional(readOnly = true)
    public ProductPageInfo getProducts(Pageable pageable) {
        Page<Product> productPage = productRepository.findAllWithCategory(pageable);

        return ProductPageInfo.from(productPage);
    }

    @Transactional
    public void deleteProduct(final Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> ProductNotFoundException.of(id));
        productRepository.delete(product);
    }
}
