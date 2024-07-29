package gift;

import gift.model.category.Category;
import gift.model.gift.Gift;
import gift.model.option.Option;
import gift.repository.gift.GiftRepository;
import gift.repository.option.OptionRepository;
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
    private GiftRepository giftRepository;

    @Autowired
    private OptionRepository optionRepository;

    private Gift gift;
    private Option option;


    @BeforeEach
    void setUp() {
        Category category = new Category(30L, "testCategory", "testCategory", "testCategory", "testCategory");
        Gift gift = new Gift("Test Gift", 1000, "test.jpg", category);
        Option option = new Option("Test Option", 1);
        gift.addOption(option);

        giftRepository.save(gift);
        optionRepository.save(option);

        this.gift = gift;
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
                    optionService.subtractOptionToGift(gift.getId(), option.getId(), subtractQuantity);
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