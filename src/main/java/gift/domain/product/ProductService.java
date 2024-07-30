package gift.domain.product;

import gift.domain.category.Category;
import gift.domain.category.JpaCategoryRepository;
import gift.domain.option.OptionService;
import gift.domain.product.dto.request.ProductRequest;
import gift.domain.product.dto.request.UpdateProductRequest;
import gift.domain.product.dto.response.ProductPageResponse;
import gift.domain.product.dto.response.ProductPageResponse.ProductForPage;
import gift.domain.product.dto.response.ProductResponse;
import gift.global.exception.BusinessException;
import gift.global.exception.ErrorCode;
import gift.global.exception.category.CategoryNotFoundException;
import gift.global.exception.product.ProductDuplicateException;
import gift.global.exception.product.ProductNotFoundException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
     * 상품 생성
     */
    public void createProduct(ProductRequest productRequest) {
        validateUniqueName(productRequest.name());
        validateCategoryExists(productRequest.categoryId());

        Product product = new Product(
            productRequest.name(),
            categoryRepository.findById(productRequest.categoryId()).get(),
            productRequest.price(),
            productRequest.description(),
            productRequest.imageUrl()
        );
        // 상품 저장
        Product savedProduct = productRepository.save(product);
        // 상품의 옵션들 저장
        optionService.addOptionsToNewProduct(savedProduct, productRequest.options());
    }

    /**
     * 상품 조회
     */
    public Product getProduct(Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException(id));

        return product;
    }

    /**
     * 전체 상픔 목록 조회 - 페이징(매개변수별)
     */
    public ProductPageResponse getProductsByPage(int page, int size, Sort sort, Long categoryId) {
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        // 카테고리 조건 X -> 모든 상품 조회
        if(categoryId.equals(0L)){
            Page<Product> productPage = productRepository.findAll(pageRequest);
            return createProductPageResponse(productPage);
        }
        // 카테고리 조건 O -> 해당 카테고리 상품 조회
        Page<Product> productPage = productRepository.findAllByCategoryId(categoryId, pageRequest);
        return createProductPageResponse(productPage);
    }

    /**
     * 상품 수정 - (현재) 상품의 옵션은 "따로" 수정
     */
    @Transactional
    public void updateProduct(Long id, UpdateProductRequest productRequest) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException(id));
        validateUniqueNameWithoutMe(id, productRequest.name());

        Category category = categoryRepository.findById(productRequest.categoryId())
            .orElseThrow(() -> new CategoryNotFoundException(productRequest.categoryId()));

        product.update(productRequest.name(), category, productRequest.price(), productRequest.description(),
            productRequest.imageUrl());
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

    public ProductPageResponse createProductPageResponse(Page<Product> productPage) {
        boolean hasNext = productPage.hasNext();
        List<ProductForPage> products = productPage.stream().map(product -> product.toProductForPage())
            .toList();
        return new ProductPageResponse(hasNext, products);
    }
}


