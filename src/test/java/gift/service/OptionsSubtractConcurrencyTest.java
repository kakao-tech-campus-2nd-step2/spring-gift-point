package gift.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import gift.exception.option.FailedRetryException;
import gift.exception.option.OptionsQuantityException;
import gift.model.Category;
import gift.model.Options;
import gift.model.Product;
import gift.repository.CategoryRepository;
import gift.repository.OptionsRepository;
import gift.repository.ProductRepository;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@EnableRetry
public class OptionsSubtractConcurrencyTest {

    @Autowired
    private OptionsRepository optionsRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private OptionsService optionsService;

    private Product savedProduct;
    private Options savedOptions;
    private static final int AVAILABLE_COUNTS = 1;
    private static final int CONCURRENT_SUB_COUNT = 1;
    private static final int CLIENT_COUNT = 2;

    @BeforeEach
    void setUp() {
        Category category = new Category("카테고리");
        Category savedCategory = categoryRepository.save(category);
        Product product = new Product("상품", 1000, "http://a.com", savedCategory);
        savedProduct = productRepository.save(product);
        Options options = new Options("옵션", AVAILABLE_COUNTS, savedProduct);
        savedOptions = optionsRepository.save(options);
    }


    @DisplayName("낙관적 락을 적용한 동시성 제어 테스트")
    @Test
    void concurrencyControllingWithOptimisticLock() throws InterruptedException {
        //given
        ExecutorService executorService = Executors.newFixedThreadPool(CLIENT_COUNT);
        CountDownLatch countDownLatch = new CountDownLatch(CLIENT_COUNT);
        AtomicInteger succeedCount = new AtomicInteger();

        // when
        for (int i = 0; i < CLIENT_COUNT; i++) {
            executorService.execute(() -> {
                try {
                    optionsService.subtractQuantity(savedOptions.getId(),
                        CONCURRENT_SUB_COUNT, savedProduct.getId());
                    succeedCount.getAndIncrement();
                } catch (OptionsQuantityException e) {
                    System.out.println("수량이 부족합니다.");
                } catch (FailedRetryException e) {
                    System.out.println("요청에 실패하였습니다.");
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();

        // then
        optionsRepository.findById(savedProduct.getId())
            .ifPresent(o -> {
                    assertThat(o.getQuantity()).isEqualTo(0);
                }
            );
        assertThat(succeedCount.get()).isEqualTo(AVAILABLE_COUNTS);

    }


}
