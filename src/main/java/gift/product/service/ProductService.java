package gift.product.service;

import gift.category.model.Category;
import gift.category.repository.CategoryRepository;
import gift.common.exception.ProductAlreadyExistsException;
import gift.option.domain.Option;
import gift.option.domain.OptionRequest;
import gift.option.repository.OptionJpaRepository;
import gift.product.model.Product;
import gift.product.repository.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final OptionJpaRepository optionJpaRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, OptionJpaRepository optionJpaRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.optionJpaRepository = optionJpaRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAllWithCategory();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
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
    public Page<Product> getProductsByPage(int page, int size, String sortBy, String direction) {
        // validation
        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("Invalid page or size");
        }

        // sorting
        Sort sort;
        if (direction.equalsIgnoreCase("asc")) {
            sort = Sort.by(sortBy).ascending();
        } else {
            sort = Sort.by(sortBy).descending();
        }

        Pageable pageable = PageRequest.of(page, size, sort);
        return productRepository.findAll(pageable);
    }

    // 카테고리 목록을 반환하는 메서드 추가
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // id에 해당하는 option들 전부 조회
    public List<Option> getOptionsByProductId(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 다음 id의 상품은 존재하지 않음 : " + id));
        return optionJpaRepository.findAllByProduct(product);
    }
}
