package gift.api.option;

import static org.assertj.core.api.Assertions.assertThat;

import gift.api.category.domain.Category;
import gift.api.category.repository.CategoryRepository;
import gift.api.option.domain.Option;
import gift.api.option.repository.OptionRepository;
import gift.api.option.service.OptionService;
import gift.api.product.domain.Product;
import gift.api.product.repository.ProductRepository;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OptionIntegrationTest {

    @Autowired
    private OptionRepository optionRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private OptionService optionService;

    @BeforeEach
    void setup() {
        Category category = new Category("category", "#FFFFFF", "url", "2상2는 바보다.");
        categoryRepository.save(category);
        Product product = new Product(category, "product", 1000, "url");
        productRepository.save(product);
        Option option = new Option(product, "option", 100);
        optionRepository.save(option);
    }

    @Test
    @DisplayName("옵션_수량_차감_동시성_테스트")
    void concurrentSubtract() throws InterruptedException {
        // given
        final int threadCount = 100;
        final ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        final CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    optionService.subtract(4L, 1);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();

        // then
        assertThat(optionRepository.findById(4L).get().getQuantity())
            .isEqualTo(0);
    }
}
