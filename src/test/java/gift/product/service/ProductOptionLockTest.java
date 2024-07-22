package gift.product.service;

import static org.assertj.core.api.Assertions.assertThat;

import gift.product.domain.Category;
import gift.product.domain.Product;
import gift.product.domain.ProductOption;
import gift.product.persistence.CategoryRepository;
import gift.product.persistence.ProductOptionRepository;
import gift.product.persistence.ProductRepository;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class ProductOptionLockTest {
    @Autowired
    private ProductOptionRepository productOptionRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductOptionService productOptionService;

    @BeforeEach
    void setUp() {
        Category category = new Category("name", "test", "Test", "Test");
        Product product = new Product("name", 1000, "URL", category);
        ProductOption productOption = new ProductOption("test", 100, product);

        categoryRepository.save(category);
        productRepository.save(product);
        productOptionRepository.save(productOption);
    }

    @Test
    @DisplayName("ProductOptionService 동시성 테스트")
    void productOptionServiceBuyProductTest() throws InterruptedException {
        //given
        final Integer THREAD_CNT = 77;
        final Long productId = 1L;
        final Long optionId = 1L;

        //when
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_CNT);
        CountDownLatch countDownLatch = new CountDownLatch(THREAD_CNT);
        for (int i = 0; i < THREAD_CNT; i++) {
            executorService.submit(() -> {
                try {
                    productOptionService.buyProduct(productId, optionId, 1);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();

        var option = productOptionRepository.findByProductIdAndId(productId, optionId).orElseThrow();
        assertThat(option.getQuantity()).isEqualTo(23);
    }
}
