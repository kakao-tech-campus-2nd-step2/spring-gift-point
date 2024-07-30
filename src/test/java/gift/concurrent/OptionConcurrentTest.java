package gift.concurrent;

import gift.domain.Option;
import gift.repository.option.OptionRepository;
import gift.service.OptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class OptionConcurrentTest {

    @Autowired
    OptionService optionService;

    @Autowired
    OptionRepository optionRepository;


    Option option;

    @BeforeEach
    void before_each(){
        option = new Option("TEST OPTION", 100);
        optionRepository.save(option);
    }

    @Test
    @DisplayName("옵션 수량 차감 동시성 테스트")
    void decreasePriceForMultiThreadTest() throws InterruptedException {
        AtomicInteger successCount = new AtomicInteger();
        int numberOfExecute = 100;
        ExecutorService service = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(numberOfExecute);

        for (int i = 0; i < numberOfExecute; i++) {
            service.execute(() -> {
                try {
                    optionService.updateOptionQuantity(option.getId(), 10);
                    successCount.getAndIncrement();
                    System.out.println("성공");
                } catch (Exception e) {
                    System.out.println(e);
                }
                latch.countDown();
            });
        }
        latch.await();

        assertThat(successCount.get()).isEqualTo(10);
    }



}
