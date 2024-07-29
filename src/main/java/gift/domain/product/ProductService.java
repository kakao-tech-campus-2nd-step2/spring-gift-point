package gift.domain.product;

import gift.domain.category.Category;
import gift.domain.category.JpaCategoryRepository;
import gift.domain.option.OptionService;
import gift.global.exception.BusinessException;
import gift.global.exception.ErrorCode;
import gift.global.exception.category.CategoryNotFoundException;
import gift.global.exception.product.ProductDuplicateException;
import gift.global.exception.product.ProductNotFoundException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final JpaProductRepository productRepository;
    private final JpaCategoryRepository categoryRepository;
    private final OptionService optionService;

    @Autowired
    public ProductService(
        JpaProductRepository jpaProductRepository,
        JpaCategoryRepository jpaCategoryRepository,
        OptionService optionService
    ) {
        this.productRepository = jpaProductRepository;
        this.categoryRepository = jpaCategoryRepository;
        this.optionService = optionService;
    }

    /**
     * 상품 추가
     */
    public void createProduct(ProductDTO productDTO) {
        validateUniqueName(productDTO.name());
        validateCategoryExists(productDTO.categoryId());

        Product product = new Product(
            productDTO.name(),
            categoryRepository.findById(productDTO.categoryId()).get(),
            productDTO.price(),
            productDTO.imageUrl()
        );
        // 상품 저장
        Product savedProduct = productRepository.save(product);
        // 옵션 저장
        optionService.addOption(savedProduct, productDTO.option());
    }

    /**
     * 특정 삼품 조회
     */
    public Product getProduct(Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException(id));

        return product;
    }

    /**
     * 전체 싱픔 목록 조회 - 페이징(매개변수별)
     */
    public Page<Product> getProductsByPage(int page, int size, Sort sort, Long categoryId) {
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        // 카테고리 조건 X
        if(categoryId.equals(0)){
            return productRepository.findAll(pageRequest);
        }
        // 카테고리 조건 O
        return productRepository.findAllByCategoryId(pageRequest);
    }

    /**
     * 상품 수정 - (현재) 상품의 옵션은 따로 수정
     */
    @Transactional
    public void updateProduct(Long id, ProductDTO productDTO) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException(id));
        validateUniqueNameWithoutMe(id, productDTO.name());

        Category category = categoryRepository.findById(productDTO.categoryId())
            .orElseThrow(() -> new CategoryNotFoundException(productDTO.categoryId()));

        product.update(productDTO.name(), category, productDTO.price(),
            productDTO.imageUrl());
    }

    /**
     * 상품 삭제
     */
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        productRepository.deleteById(id);
    }

    /**
     * 다수의 상품 삭제
     */
    public void deleteProductsByIds(List<Long> productIds) {
        if (productIds.isEmpty()) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "삭제할 상품을 선택하세요.");
        }

        productRepository.deleteAllByIdIn(productIds);
    }


    // 상품 생성 시 이름 중복 확인
    private void validateUniqueName(String productName) {
        if (productRepository.existsByName(productName)) {
            throw new ProductDuplicateException(productName);
        }
    }

    // 상품 수정 시 이름 중복 확인
    private void validateUniqueNameWithoutMe(Long productId, String productName) {
        productRepository.findByName(productName)
            .filter(product -> !product.getId().equals(productId))
            .ifPresent(product -> {
                throw new ProductDuplicateException(productName);
            });
    }

    private void validateCategoryExists(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new CategoryNotFoundException(categoryId);
        }
    }
}


