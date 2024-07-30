package gift.service;

import static gift.util.CategoryFixture.createCategory;
import static gift.util.OptionFixture.createOption;
import static gift.util.ProductFixture.createProduct;
import static org.assertj.core.api.Assertions.assertThat;

import gift.domain.Category;
import gift.domain.Option;
import gift.domain.Product;
import gift.repository.CategoryRepository;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OptionLockTest {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OptionRepository optionRepository;
    @Autowired
    private OptionService optionService;

    @DisplayName("option 구매 동시성 테스트")
    @Test
    void buyOption() throws InterruptedException {
        // given
        Category category = createCategory();
        categoryRepository.save(category);
        Product product = createProduct(category);
        productRepository.save(product);
        Option option = optionRepository.save(createOption(product));

        int numberOfThreads = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch countDownLatch = new CountDownLatch(numberOfThreads);

        // when
        for (int i = 0; i < numberOfThreads; i++) {
            executorService.execute(() -> {
                try {
                    optionService.buyOption(option.getId(), 1);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();

        // then
        Option updatedOption = optionRepository.findById(option.getId()).get();
        assertThat(updatedOption.getQuantity()).isEqualTo(0);
    }
}
