package gift.service;

import gift.constants.ErrorMessage;
import gift.dto.ProductRequest;
import gift.dto.ProductResponse;
import gift.entity.Category;
import gift.entity.Option;
import gift.entity.Product;
import gift.repository.CategoryJpaDao;
import gift.repository.OptionJpaDao;
import gift.repository.ProductJpaDao;
import java.util.NoSuchElementException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductJpaDao productJpaDao;
    private final CategoryJpaDao categoryJpaDao;
    private final OptionJpaDao optionJpaDao;

    public ProductService(ProductJpaDao productJpaDao, CategoryJpaDao categoryJpaDao,
        OptionJpaDao optionJpaDao) {
        this.productJpaDao = productJpaDao;
        this.categoryJpaDao = categoryJpaDao;
        this.optionJpaDao = optionJpaDao;
    }

    /**
     * 상품을 먼저 등록한 뒤, 옵션들을 추가. 상품 등록이 실패하여 savedProduct가 null이면 옵션을 추가하지 않음.
     */
    @Transactional
    public void addProduct(ProductRequest productRequest) {
        assertProductNotDuplicate(productRequest.getName());
        Category category = findCategoryById(productRequest.getCategoryId());
        Product savedProduct = productJpaDao.save(new Product(productRequest, category));
        addOptions(productRequest, savedProduct);
    }

    /**
     * 상품과 카테고리 존재하는지 확인 후 수정.
     */
    @Transactional
    public void editProduct(ProductRequest productRequest) {
        Product targetProduct = findProductById(productRequest.getId());
        Category category = findCategoryById(productRequest.getCategoryId());
        targetProduct.updateProduct(productRequest, category);
    }

    public void deleteProduct(Long id) {
        findProductById(id);
        productJpaDao.deleteById(id);
    }

    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        return productJpaDao.findAll(pageable).map(ProductResponse::new);
    }

    public ProductResponse getProduct(Long id) {
        Product product = findProductById(id);
        return new ProductResponse(product);
    }

    /**
     * 상품 추가 한 뒤, 함께 전달받은 옵션들을 등록하는 과정.
     *
     * @param productRequest 받은 요청
     * @param savedProduct   요청에 따라 저장된 상품 객체
     */
    private void addOptions(ProductRequest productRequest, Product savedProduct) {
        if (savedProduct == null) {
            throw new NoSuchElementException(ErrorMessage.PRODUCT_NOT_EXISTS_MSG);
        }
        productRequest.getOptions().forEach(optionSaveRequest -> {
            Option option = optionSaveRequest.toEntity(savedProduct);

            if (savedProduct.isOptionNameDuplicate(option)) {
                throw new IllegalArgumentException(ErrorMessage.OPTION_NAME_DUPLICATE_MSG);
            }

            savedProduct.addOption(option);
            optionJpaDao.save(option);
        });
    }

    /**
     * 해당 상품이 존재하면 반환. 아니면 NoSuchElementException
     */
    private Product findProductById(Long id) {
        return productJpaDao.findById(id)
            .orElseThrow(() -> new NoSuchElementException(ErrorMessage.PRODUCT_NOT_EXISTS_MSG));
    }

    private Category findCategoryById(Long categoryId) {
        return categoryJpaDao.findById(categoryId)
            .orElseThrow(() -> new NoSuchElementException(ErrorMessage.CATEGORY_NOT_EXISTS_MSG));
    }

    private void assertProductNotDuplicate(String productName) {
        productJpaDao.findByName(productName)
            .ifPresent(v -> {
                throw new IllegalArgumentException(ErrorMessage.PRODUCT_ALREADY_EXISTS_MSG);
            });
    }
}
