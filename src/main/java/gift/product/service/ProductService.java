package gift.product.service;

import gift.category.domain.Category;
import gift.category.repository.CategoryRepository;
import gift.common.exception.ProductAlreadyExistsException;
import gift.option.domain.Option;
import gift.option.repository.OptionJpaRepository;
import gift.product.domain.Product;
import gift.product.dto.ProductRequest;
import gift.product.dto.ProductResponse;
import gift.product.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@Validated
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAllWithCategory();
    }

    public ProductResponse getProductDTOById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product with id " + id + " not found"));

        return new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getImageUrl(), product.getCategory().getId());
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product with id " + id + " not found"));
    }

    @Transactional
    public void createProduct(Product product, Long CategoryId) {
        // 영속화된 category
        Category category = categoryRepository.findById(CategoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category with id " + CategoryId + " not found"));

        // product에 category 설정
        product.setCategory(category);

        // 양방향 설정
        category.addProduct(product);

        // validate
        checkForDuplicateProduct(product, category);

        // 저장 (category에도 변동사항 반영됨)
        productRepository.save(product);
    }

    @Transactional
    public void updateProduct(Long id, Product product, Long CategoryId) {
        // 영속화된 category
        Category category = categoryRepository.findById(CategoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category with id " + CategoryId + " not found"));

        // validate
        checkForDuplicateProduct(product, category);

        // 영속화된 Product
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product with id " + id + " not found"));
        existingProduct.setId(id);
        existingProduct.setName(product.getName());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setImageUrl(product.getImageUrl());
        existingProduct.setCategory(category);

        // 양방향 설정
        category.addProduct(existingProduct);

        productRepository.save(existingProduct);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product with id " + id + " not found"));
        productRepository.delete(existingProduct);
    }

    // 중복 상품 검사
    public void checkForDuplicateProduct(Product product, Category category) {
        List<Product> products = productRepository.findAll();
        for (Product p : products) {
            if (p.equals(product) && p.getCategory() == category) {
                throw new ProductAlreadyExistsException(product.getName());
            }
        }
    }

    // 페이지네이션 기능
    @Transactional(readOnly = true)
    public Page<ProductResponse> getProductsByPage(int page, int size, String sortBy, String direction, Long categoryId) {
        // validation
        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("Invalid page or size");
        }

        // sorting
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        // paging
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Product> productPage;

        // 카테고리가 없는 경우 전체 상품 조회
        if (categoryId == null) {
            productPage = productRepository.findAll(pageable);
        } else {
            productPage = productRepository.findAllByCategoryId(categoryId, pageable);
        }

        // Product 엔티티를 ProductResponse DTO로 변환
        return productPage.map(product ->
                new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getImageUrl(), product.getCategory().getId())
        );
    }
}
