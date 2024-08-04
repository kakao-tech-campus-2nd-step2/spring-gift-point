package gift.service;

import gift.dto.option.OptionRequestDTO;
import gift.entity.Option;
import gift.repository.OptionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class OptionServiceLockTest {

    @Autowired
    private OptionRepository optionRepository;
    @Autowired
    private OptionService optionService;
    @Autowired
    private UserService userService;

    @Test
    @DisplayName("동시에 100개의 요청으로 재고를 감소시킨다.")
    void decrease_100_request() throws InterruptedException {
        // given
        Option option = optionRepository.saveAndFlush(new Option(new OptionRequestDTO("test", 100)));

        final int threadCount = 100;
        final ExecutorService executorService = Executors.newFixedThreadPool(32);
        final CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    optionService.subtract(option.getId(), 1);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        Option updatedOption = optionRepository.findById(option.getId()).orElseThrow();

        // then
        assertThat(updatedOption.getQuantity()).isEqualTo(0);
    }
}
