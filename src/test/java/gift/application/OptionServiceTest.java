package gift.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gift.application.product.dto.OptionCommand.Purchase;
import gift.application.product.service.OptionService;
import gift.application.product.service.ProductService;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OptionServiceTest {

    @Autowired
    private OptionService optionService;

    @Autowired
    private ProductService productService;

    @Test
    @DisplayName("동시에_1000개_쓰레드_구매요청")
    void 동시에_1000개_쓰레드_구매요청() throws InterruptedException {
        // given
        int threadCount = 1000;
        ExecutorService excuterService = Executors.newFixedThreadPool(threadCount);
        // when

        for (int i = 0; i < threadCount; i++) {
            excuterService.submit(() -> {
                try {
                    optionService.purchaseOption(new Purchase(1L, 1));
                } catch (Exception e) {
                    System.out.println(e.getMessage() + "retry");
                }

            });
        }

        excuterService.shutdown();

        // then
        assertThat(optionService.findOptionById(1L).getQuantity()).isEqualTo(0);

    }

    @Test
    @DisplayName("동시에_1001개_쓰레드_구매요청_실패")
    void 동시에_1001개_쓰레드_구매요청_실패() throws InterruptedException {
        // given
        int threadCount = 1001;
        ExecutorService excuterService = Executors.newFixedThreadPool(threadCount);
        // when

        for (int i = 0; i < threadCount; i++) {
            excuterService.submit(() -> {
                try {
                    optionService.purchaseOption(new Purchase(1L, 1));
                } catch (Exception e) {
                    System.out.println(e.getMessage() + "retry");
                }

            });
        }

        excuterService.shutdown();
        // then
        assertThat(optionService.findOptionById(1L).getQuantity()).isEqualTo(0);

    }

    @Test
    @DisplayName("동시에_1002개_쓰레드_구매요청_실패")
    void 동시에_1002개_쓰레드_구매요청_실패() throws InterruptedException {
        // given
        int threadCount = 1002;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        // when
        List<CompletableFuture<Void>> futures = IntStream.range(0, threadCount)
            .mapToObj(i -> CompletableFuture.runAsync(() -> {
                try {
                    optionService.purchaseOption(new Purchase(1L, 1));
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException(e);
                }
            }, executorService))
            .toList();

        CompletableFuture<Void> allOf = CompletableFuture.allOf(
            futures.toArray(new CompletableFuture[0]));

        // then
        RuntimeException thrown = assertThrows(RuntimeException.class, allOf::join);
        assertThat(thrown.getCause()).isInstanceOf(RuntimeException.class)
            .hasMessage("java.lang.IllegalArgumentException: 재고가 부족합니다.");

        executorService.shutdown();

        assertThat(optionService.findOptionById(1L).getQuantity()).isEqualTo(0);
    }

}
