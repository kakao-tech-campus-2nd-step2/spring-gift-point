package gift.service;

import gift.dto.*;
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

    private Option createOption(String optionName) {
        OptionRequestDto optionRequestDto = new OptionRequestDto(optionName);
        return optionRepository.save(new Option(new OptionName(optionRequestDto.getName())));
    }

    private ProductOption createProductOption(String optionName, int quantity) {
        Long categoryId = createCategory();
        Product product = createProduct(categoryId);
        Option option = createOption(optionName);

        return productOptionRepository.save(new ProductOption(product, option, quantity));
    }

    @Test
    @Rollback
    public void 상품_옵션_추가_성공() {
        Long categoryId = createCategory();
        Product product = createProduct(categoryId);
        Option option = createOption("옵션1");

        ProductOptionRequestDto productOptionRequestDto = new ProductOptionRequestDto(product.getId(), option.getId(), 10);
        ProductOptionResponseDto createdProductOption = productOptionService.addProductOption(productOptionRequestDto);

        assertNotNull(createdProductOption);
        assertEquals("상품1", createdProductOption.getProductName());
        assertEquals("옵션1", createdProductOption.getOptionName());
        assertEquals(10, createdProductOption.getQuantity());
    }

    @Test
    @Rollback
    public void 상품_옵션_조회_성공() {
        Long categoryId = createCategory();
        Product product = createProduct(categoryId);
        Option option1 = createOption("옵션1");
        Option option2 = createOption("옵션2");

        productOptionRepository.save(new ProductOption(product, option1, 10));
        productOptionRepository.save(new ProductOption(product, option2, 20));

        List<ProductOptionResponseDto> productOptions = productOptionService.getProductOptions(product.getId());

        assertEquals(2, productOptions.size());
        assertEquals("옵션1", productOptions.get(0).getOptionName());
        assertEquals(10, productOptions.get(0).getQuantity());
        assertEquals("옵션2", productOptions.get(1).getOptionName());
        assertEquals(20, productOptions.get(1).getQuantity());
    }

    @Test
    @Rollback
    public void 상품_옵션_수정_성공() {
        ProductOption productOption = createProductOption("옵션1", 10);

        ProductOptionRequestDto updateRequestDto = new ProductOptionRequestDto(productOption.getProduct().getId(), productOption.getOption().getId(), 20);
        ProductOptionResponseDto updatedProductOption = productOptionService.updateProductOption(productOption.getId(), updateRequestDto);

        assertNotNull(updatedProductOption);
        assertEquals(20, updatedProductOption.getQuantity());
    }

    @Test
    @Rollback
    public void 상품_옵션_삭제_성공() {
        ProductOption productOption = createProductOption("옵션1", 10);

        productOptionService.deleteProductOption(productOption.getId());

        assertFalse(productOptionRepository.findById(productOption.getId()).isPresent());
    }

    @Test
    @Rollback
    public void 상품_옵션_수량_감소_성공() {
        ProductOption productOption = createProductOption("옵션1", 10);

        productOptionService.decreaseProductOptionQuantity(productOption.getId(), 5);

        ProductOption updatedProductOption = productOptionRepository.findById(productOption.getId()).orElseThrow();
        assertEquals(5, updatedProductOption.getQuantity());
    }

    @Test
    @Rollback
    public void 상품_옵션_수량_감소_실패_수량부족() {
        ProductOption productOption = createProductOption("옵션1", 10);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            productOptionService.decreaseProductOptionQuantity(productOption.getId(), 15);
        });

        assertEquals(ErrorCode.INSUFFICIENT_QUANTITY, exception.getErrorCode());
    }

    @Test
    @Rollback
    public void 상품_옵션_수량_감소_실패_음수() {
        ProductOption productOption = createProductOption("옵션1", 10);

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

        ProductOption productOption = createProductOption("옵션1", initialQuantity);

        productOptionRepository.save(productOption);

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
