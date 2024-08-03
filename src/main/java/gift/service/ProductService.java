package gift.service;

import gift.DTO.product.ProductPageResponse;
import gift.DTO.product.ProductRequest;
import gift.DTO.product.ProductResponse;
import gift.domain.Category;
import gift.domain.Product;
import gift.repository.ProductRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    @Autowired
    public ProductService(ProductRepository productRepository, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductResponse> response = products.stream()
                                        .map(ProductResponse::fromEntity)
                                        .toList();
        return response;
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> findPagedProductsByCategoryId(Pageable pageable, Long categoryId) {
        Page<Product> productPage;
        if (categoryId == null) {
            productPage = productRepository.findAll(pageable);
        } else {
            Category category = categoryService.getCategoryById(categoryId);
            productPage = productRepository.findByCategory(category, pageable);
        }

        return productPage.map(ProductResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        Product product = getProductByIdOrThrow(id);
        ProductResponse response = ProductResponse.fromEntity(product);
        return response;
    }

    @Transactional
    public ProductResponse addProduct(ProductRequest productRequest) {
        productRepository.findByName(productRequest.getName())
                            .ifPresent(p -> {
                                throw new RuntimeException("Product name must be unique");
                            });

        Category category = categoryService.getCategoryById(productRequest.getCategoryId());
        Product productEntity = productRequest.toEntity(category);

        productRepository.save(productEntity);

        ProductResponse response = ProductResponse.fromEntity(productEntity);
        return response;
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest updatedProduct) {
        Product product = getProductByIdOrThrow(id);
        Category newCategory = categoryService.getCategoryById(updatedProduct.getCategoryId());

        product.setName(updatedProduct.getName());
        product.setPrice(updatedProduct.getPrice());
        product.setImageUrl(updatedProduct.getImageUrl());
        product.setCategory(newCategory);

        productRepository.save(product);

        ProductResponse response = ProductResponse.fromEntity(product);
        return response;
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = getProductByIdOrThrow(id);
        productRepository.delete(product);
    }

    private Product getProductByIdOrThrow(Long id) {
        return productRepository.findById(id).orElseThrow(() ->
            new RuntimeException("Product not found with id: " + id));
    }
}
