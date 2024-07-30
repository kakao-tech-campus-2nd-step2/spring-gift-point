package gift.domain.product.service;

import static org.assertj.core.api.Assertions.assertThat;

import gift.domain.product.entity.Category;
import gift.domain.product.entity.Option;
import gift.domain.product.entity.Product;
import gift.domain.product.repository.CategoryJpaRepository;
import gift.domain.product.repository.OptionJpaRepository;
import gift.domain.product.repository.ProductJpaRepository;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OptionServiceConcurrencyTest {

    @Autowired
    private OptionService optionService;

    @Autowired
    private CategoryJpaRepository categoryJpaRepository;

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Autowired
    private OptionJpaRepository optionJpaRepository;

    @Test
    @DisplayName("옵션 수량 차감 동시성 테스트")
    void subtractQuantity() throws InterruptedException {
        // given
        Category category = categoryJpaRepository.findById(1L).orElseThrow();
        Product product = new Product(null, category, "testProduct", 10000, "https://test.com");
        Option option = new Option(null, product, "사과맛", 30);
        product.addOption(option);
        productJpaRepository.save(product);
        Option savedOption = optionJpaRepository.save(option);

        int threadCount = 30;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    optionService.subtractQuantity(savedOption.getId(), 1);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        // then
        Option foundOption = optionJpaRepository.findById(savedOption.getId()).orElseThrow();
        assertThat(foundOption.getQuantity()).isEqualTo(0);
    }
}
