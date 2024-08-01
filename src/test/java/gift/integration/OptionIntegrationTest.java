package gift.integration;

import static org.assertj.core.api.Assertions.assertThat;

import gift.product.category.entity.Category;
import gift.product.category.repository.CategoryJpaRepository;
import gift.product.entity.Product;
import gift.product.option.entity.Option;
import gift.product.option.repository.OptionJpaRepository;
import gift.product.option.service.OptionService;
import gift.product.repository.ProductJpaRepository;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OptionIntegrationTest {

    @Autowired
    private OptionService optionService;

    @Autowired
    private CategoryJpaRepository categoryRepository;

    @Autowired
    private OptionJpaRepository optionRepository;

    @Autowired
    private ProductJpaRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        optionRepository.deleteAll();
        saveData();
    }

    @Test
    @DisplayName("option subtract at the same time test")
    void optionSubtractAtTheSameTimeTest() throws InterruptedException {
        // given
        final int threadCount = 10;
        final Long optionId = optionRepository.findAll().getFirst().getId();
        final int initialQuantity = optionRepository.findById(optionId).orElseThrow().getQuantity();
        final int subtractionQuantity = 1;

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    optionService.subtractOptionQuantity(optionId, subtractionQuantity);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        // then
        Option updatedOption = optionRepository.findById(optionId).orElseThrow();
        assertThat(updatedOption.getQuantity()).isEqualTo(
            initialQuantity - subtractionQuantity * threadCount);
    }

    @Test
    @DisplayName("option subtract 3 for 10 times test - 9회 성공 1회 실패")
    void optionSubtract3For10TimesTest() throws InterruptedException {
        // given
        final int threadCount = 10;
        final Long optionId = optionRepository.findAll().getFirst().getId();
        final int initialQuantity = optionRepository.findById(optionId).orElseThrow().getQuantity();
        final int subtractionQuantity = 3;

        AtomicInteger failCount = new AtomicInteger(0);

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    optionService.subtractOptionQuantity(optionId, subtractionQuantity);
                } catch (Exception e) {
                    failCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        // then
        Option updatedOption = optionRepository.findById(optionId).orElseThrow();
        assertThat(failCount.get()).isEqualTo(1);
        assertThat(updatedOption.getQuantity()).isEqualTo(
            initialQuantity - subtractionQuantity * (threadCount - failCount.get()));
    }


    private void saveData() {
        Category category1 = new Category("Category 1", "#123456", "", "image");
        categoryRepository.save(category1);
        Product product1 = new Product(
            "Product1",
            1000,
            "image",
            category1
        );
        productRepository.save(product1);
        Option option1 = new Option("Option 1", 29, product1);
        optionRepository.save(option1);
    }
}
