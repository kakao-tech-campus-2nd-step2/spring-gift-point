package gift.product.service;

import gift.product.dto.category.CategoryIdAndName;
import gift.product.dto.option.OptionDto;
import gift.product.dto.option.OptionResponse;
import gift.product.dto.product.ProductRequest;
import gift.product.dto.product.ProductResponse;
import gift.product.dto.product.ProductUpdateRequest;
import gift.product.model.Category;
import gift.product.model.Option;
import gift.product.model.Product;
import gift.product.repository.CategoryRepository;
import gift.product.repository.OptionRepository;
import gift.product.repository.ProductRepository;
import java.util.List;
import java.util.NoSuchElementException;
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

    public List<ProductResponse> getProductAll(Pageable pageable) {
        return productRepository.findAll(pageable).stream().map(this::getProductResponse).toList();
    }

    public List<ProductResponse> getProductAll(Pageable pageable, Long categoryId) {
        return productRepository.findAllByCategoryId(pageable, categoryId).stream().map(this::getProductResponse).toList();
    }

    public ProductResponse getProduct(Long id) {
        Product product = getValidatedProduct(id);
        List<OptionResponse> optionResponses = optionRepository.findAllByProductId(id);
        CategoryIdAndName categoryIdAndName = new CategoryIdAndName(product.getCategory().getId(), product.getCategory().getName());
        return new ProductResponse(product.getId(), product.getName(),
            product.getPrice(), product.getImageUrl(), categoryIdAndName, optionResponses);
    }

    @Transactional
    public Product insertProduct(ProductRequest clientProductRequest) {
        Category category = getValidatedCategory(clientProductRequest.categoryId());
        Product product = new Product(clientProductRequest.name(), clientProductRequest.price(), clientProductRequest.imageUrl(),
            category);
        Product savedProduct = productRepository.save(product);

        for (OptionDto optionDto : clientProductRequest.options()) {
            optionRepository.save(new Option(optionDto.name(), optionDto.quantity(), savedProduct));
        }

        return savedProduct;
    }

    @Transactional
    public Product updateProduct(Long id, ProductUpdateRequest productUpdateRequest) {
        getValidatedProduct(id);
        Category category = getValidatedCategory(productUpdateRequest.categoryId());

        Product product = new Product(id, productUpdateRequest.name(), productUpdateRequest.price(),
            productUpdateRequest.imageUrl(), category);

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

    private Category getValidatedCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
            .orElseThrow(() -> new NoSuchElementException("해당 카테고리가 존재하지 않습니다."));
    }

    private ProductResponse getProductResponse(Product product) {
        CategoryIdAndName categoryIdAndName = new CategoryIdAndName(product.getCategory().getId(), product.getCategory().getName());
        List<OptionResponse> optionResponses = optionRepository.findAllByProductId(product.getId());

        return new ProductResponse(product.getId(),
            product.getName(),
            product.getPrice(),
            product.getImageUrl(),
            categoryIdAndName,
            optionResponses);
    }
}
