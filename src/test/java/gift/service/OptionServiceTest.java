package gift.service;

import static org.assertj.core.api.Assertions.assertThat;

import gift.dto.CategoryDto;
import gift.dto.OptionResponse;
import gift.dto.OptionSaveRequest;
import gift.dto.OptionSubtractRequest;
import gift.dto.ProductRequest;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("/sql/truncateIdentity.sql")
class OptionServiceTest {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;
    @Autowired
    private OptionService optionService;

    private final CategoryDto categoryDto = new CategoryDto(null, "선물", "white", "http", "cake");
    private final OptionSaveRequest optionRequest = new OptionSaveRequest("라지", 1000, 1L);
    private final ProductRequest productRequest = new ProductRequest(1L, "케잌", 45000L, "http", 1L,
        "선물",
        List.of(optionRequest));
    private final OptionSubtractRequest subtractRequest = new OptionSubtractRequest(1L, 1, 1L);

    @Test
    @DisplayName("옵션 개수 차감 테스트")
    void subtractOption() {
        categoryService.addCategory(categoryDto);
        productService.addProduct(productRequest);

        optionService.subtractOption(subtractRequest);

        OptionResponse option = optionService.findOptionById(1L);
        System.out.println("option = " + option.getQuantity());
        assertThat(option.getQuantity())
            .isEqualTo(optionRequest.getQuantity() - subtractRequest.getQuantity());
    }

    @Test
    @DisplayName("옵션 개수 차감 동시성 테스트")
    void multiThreadsTest() throws InterruptedException {
        categoryService.addCategory(categoryDto);
        productService.addProduct(productRequest);

        final int threadCount = 1000;

        ExecutorService executorService = Executors.newFixedThreadPool(30);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    optionService.subtractOption(subtractRequest);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        OptionResponse option = optionService.findOptionById(1L);
        assertThat(option.getQuantity())
            .isEqualTo(0);
    }
}