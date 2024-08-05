package gift;

import gift.model.category.Category;
import gift.model.option.Option;
import gift.model.product.Product;
import gift.repository.option.OptionRepository;
import gift.repository.product.ProductRepository;
import gift.service.option.OptionService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class OptionSubtractConcurrencyTest {

    @Autowired
    private OptionService optionService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OptionRepository optionRepository;

    private Product product;
    private Option option;


    @BeforeEach
    void setUp() {
        Category category = new Category(30L, "testCategory", "testCategory", "testCategory", "testCategory");
        Product product = new Product("Test Gift", 1000, "test.jpg", category);
        Option option = new Option("Test Option", 1);
        product.addOption(option);

        productRepository.save(product);
        optionRepository.save(option);

        this.product = product;
        this.option = option;
    }

    @Test
    @DisplayName("낙관적 락을 이용한 옵션 수량차감 동시성 테스트. 수량이 1개일때 수량차감 성공하는 스레드는 1개여야함.")
    void testSubtractOptionToGiftWithOptimisticLock() throws InterruptedException {
        int subtractQuantity = 1;
        int threadCount = 10;

        CountDownLatch latch = new CountDownLatch(threadCount);
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    optionService.subtractOptionToGift(product.getId(), option.getId(), subtractQuantity);
                    successCount.incrementAndGet();
                } catch (OptimisticLockingFailureException e) {
                    System.out.println("낙관적 락 발생: " + e.getMessage());
                    failCount.incrementAndGet();
                } catch (Exception e) {
                    System.out.println("예외 발생: " + e.getMessage());
                    failCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Option updatedOption = optionRepository.findById(option.getId()).orElseThrow();
        System.out.println("최초 수량: 1000");
        System.out.println("최종 수량: " + updatedOption.getQuantity());
        System.out.println("성공한 스레드 수: " + successCount.get());
        System.out.println("실패한 스레드 수: " + failCount.get());

        assertEquals(100, successCount.get());
        assertEquals(threadCount - 1, failCount.get());
        assertEquals(0, updatedOption.getQuantity());
    }
}