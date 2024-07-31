package gift.service;

import gift.converter.StringToUrlConverter;
import gift.domain.Category;
import gift.domain.Product;
import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import gift.repository.WishProductRepository;
import gift.web.dto.request.product.CreateProductRequest;
import gift.web.dto.request.product.UpdateProductRequest;
import gift.web.dto.response.product.CreateProductResponse;
import gift.web.dto.response.product.ReadAllProductsResponse;
import gift.web.dto.response.product.ReadProductResponse;
import gift.web.dto.response.product.UpdateProductResponse;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final WishProductRepository wishProductRepository;
    private final ProductOptionService productOptionService;

    public ProductService(ProductRepository productRepository,
        CategoryRepository categoryRepository,
        WishProductRepository wishProductRepository, ProductOptionService productOptionService) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.wishProductRepository = wishProductRepository;
        this.productOptionService = productOptionService;
    }

    @Transactional
    public CreateProductResponse createProduct(CreateProductRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
            .orElseThrow(NoSuchElementException::new);

        Product product = request.toEntity(category);
        Product savedProduct = productRepository.save(product);

        productOptionService.createInitialOptions(savedProduct.getId(), request.getProductOptions());

        return CreateProductResponse.fromEntity(savedProduct);
    }

    public ReadProductResponse readProductById(Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(NoSuchElementException::new);
        return ReadProductResponse.fromEntity(product);
    }

    public ReadAllProductsResponse readProductsByCategoryId(Long categoryId, Pageable pageable) {
        List<ReadProductResponse> products = productRepository.findByCategoryId(categoryId, pageable)
            .stream()
            .map(ReadProductResponse::fromEntity)
            .toList();
        return ReadAllProductsResponse.from(products);
    }

    public ReadAllProductsResponse readAllProducts() {
        List<ReadProductResponse> products = productRepository.findAll()
            .stream()
            .map(ReadProductResponse::fromEntity)
            .toList();
        return ReadAllProductsResponse.from(products);
    }

    public ReadAllProductsResponse readAllProducts(Pageable pageable) {
        List<ReadProductResponse> products = productRepository.findAll(pageable)
            .stream()
            .map(ReadProductResponse::fromEntity)
            .toList();
        return ReadAllProductsResponse.from(products);
    }

    @Transactional
    public UpdateProductResponse updateProduct(Long id, UpdateProductRequest request) {
        Product product = productRepository.findById(id)
            .orElseThrow(NoSuchElementException::new);

        product.updateBasicInfo(request.getName(), request.getPrice(), StringToUrlConverter.convert(request.getImageUrl()));
        return UpdateProductResponse.from(product);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(NoSuchElementException::new);
        wishProductRepository.deleteAllByProductId(id);
        productOptionService.deleteAllOptionsByProductId(id);
        productRepository.delete(product);
    }
}
