package gift.service;

import gift.dto.CategoryRequestDto;
import gift.dto.ProductOptionRequestDto;
import gift.dto.ProductOptionResponseDto;
import gift.dto.ProductRequestDto;
import gift.entity.*;
import gift.exception.BusinessException;
import gift.exception.ErrorCode;
import gift.repository.CategoryRepository;
import gift.repository.OptionRepository;
import gift.repository.ProductOptionRepository;
import gift.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ProductOptionServiceTest {

    @Autowired
    private ProductOptionRepository productOptionRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private OptionService optionService;

    @Autowired
    private ProductOptionService productOptionService;

    @AfterEach
    public void tearDown() {
        productOptionRepository.deleteAll();
        productRepository.deleteAll();
        optionRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    private Long createCategory() {
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto("테스트카테고리", "#FF0000", "https://example.com/test.png", "테스트 카테고리");
        return categoryService.addCategory(categoryRequestDto).getId();
    }

    private Product createProduct(Long categoryId) {
        ProductRequestDto productRequestDto = new ProductRequestDto("상품1", 1000, "https://example.com/product1.jpg", categoryId);
        return productRepository.save(new Product(new ProductName(productRequestDto.getName()), productRequestDto.getPrice(), productRequestDto.getImageUrl(), categoryService.getCategoryEntityById(categoryId)));
    }

    private ProductOption createProductOption(Long productId, String optionName, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND, "ID: " + productId));
        Option option = optionRepository.findByName(new OptionName(optionName))
                .orElseGet(() -> optionRepository.save(new Option(new OptionName(optionName))));
        return productOptionRepository.save(new ProductOption(product, option, quantity));
    }

    @Test
    @Rollback
    public void 상품_옵션_추가_성공() {
        Long categoryId = createCategory();
        Long productId = createProduct(categoryId).getId();
        String optionName = "옵션1";
        int quantity = 10;

        ProductOptionRequestDto productOptionRequestDto = new ProductOptionRequestDto(new OptionName(optionName), quantity);
        ProductOptionResponseDto createdProductOption = productOptionService.addProductOption(productId, productOptionRequestDto);

        assertNotNull(createdProductOption);
        assertEquals(productId, createdProductOption.getProductId());
        assertEquals(optionName, createdProductOption.getOptionName());
        assertEquals(quantity, createdProductOption.getQuantity());
    }

    @Test
    @Rollback
    public void 상품_옵션_조회_성공() {
        Long categoryId = createCategory();
        Long productId = createProduct(categoryId).getId();

        createProductOption(productId, "옵션1", 10);
        createProductOption(productId, "옵션2", 20);

        List<ProductOptionResponseDto> productOptions = productOptionService.getProductOptions(productId);

        assertEquals(2, productOptions.size());
        assertEquals("옵션1", productOptions.get(0).getOptionName());
        assertEquals(10, productOptions.get(0).getQuantity());
        assertEquals("옵션2", productOptions.get(1).getOptionName());
        assertEquals(20, productOptions.get(1).getQuantity());
    }

    @Test
    @Rollback
    public void 상품_옵션_수정_성공() {
        Long categoryId = createCategory();
        Long productId = createProduct(categoryId).getId();
        ProductOption productOption = createProductOption(productId, "옵션1", 10);

        ProductOptionRequestDto updateRequestDto = new ProductOptionRequestDto(new OptionName("옵션1"), 20);
        ProductOptionResponseDto updatedProductOption = productOptionService.updateProductOption(productId, productOption.getId(), updateRequestDto);

        assertNotNull(updatedProductOption);
        assertEquals(20, updatedProductOption.getQuantity());
    }

    @Test
    @Rollback
    public void 상품_옵션_삭제_성공() {
        Long categoryId = createCategory();
        Long productId = createProduct(categoryId).getId();
        ProductOption productOption = createProductOption(productId, "옵션1", 10);

        productOptionService.deleteProductOption(productId, productOption.getId());

        assertFalse(productOptionRepository.findById(productOption.getId()).isPresent());
    }

    @Test
    @Rollback
    public void 상품_옵션_수량_감소_성공() {
        Long categoryId = createCategory();
        Long productId = createProduct(categoryId).getId();
        ProductOption productOption = createProductOption(productId, "옵션1", 10);

        productOptionService.decreaseProductOptionQuantity(productOption.getId(), 5);

        ProductOption updatedProductOption = productOptionRepository.findById(productOption.getId()).orElseThrow();
        assertEquals(5, updatedProductOption.getQuantity());
    }

    @Test
    @Rollback
    public void 상품_옵션_수량_감소_실패_수량부족() {
        Long categoryId = createCategory();
        Long productId = createProduct(categoryId).getId();
        ProductOption productOption = createProductOption(productId, "옵션1", 10);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            productOptionService.decreaseProductOptionQuantity(productOption.getId(), 15);
        });

        assertEquals(ErrorCode.INSUFFICIENT_QUANTITY, exception.getErrorCode());
    }

    @Test
    @Rollback
    public void 상품_옵션_수량_감소_실패_음수() {
        Long categoryId = createCategory();
        Long productId = createProduct(categoryId).getId();
        ProductOption productOption = createProductOption(productId, "옵션1", 10);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            productOptionService.decreaseProductOptionQuantity(productOption.getId(), -1);
        });

        assertEquals(ErrorCode.INVALID_DECREASE_QUANTITY, exception.getErrorCode());
    }

    @Test
    @Rollback
    public void 동시성_테스트_상품_옵션_수량_감소() throws InterruptedException {
        int initialQuantity = 100;
        int decreaseAmount = 1;
        int threadCount = 5;

        Long categoryId = createCategory();
        Long productId = createProduct(categoryId).getId();
        ProductOption productOption = createProductOption(productId, "옵션1", initialQuantity);

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        Runnable task = () -> {
            try {
                productOptionService.decreaseProductOptionQuantity(productOption.getId(), decreaseAmount);
            } finally {
                latch.countDown();
            }
        };

        for (int i = 0; i < threadCount; i++) {
            executorService.execute(task);
        }

        latch.await();
        executorService.shutdown();

        ProductOption updatedProductOption = productOptionRepository.findById(productOption.getId()).orElseThrow();
        assertEquals(initialQuantity - threadCount * decreaseAmount, updatedProductOption.getQuantity());
    }
}