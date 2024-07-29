package gift;

import gift.domain.Category;
import gift.domain.Option;
import gift.domain.Product;
import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import gift.service.ProductService;
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
public class ProductServiceConcurrencyTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @DisplayName("[동시성 테스트] 옵션 수량이 1개이며 요청당 수량 1개를 차감하려고 할 때 10명이 동시에 접근할 경우, 딱 한명만 옵션 수량을 차감할 수 있다.")
    @Test
    public void concurrentSubtractionWithTenThreads() throws InterruptedException {
        // given
        Long initialQuantity = 1L;
        Long subtractAmount = 1L;
        int threadCount = 10;

        Product product = createProduct("테스트", "상품1", 100, "https://test.com", "옵션1", initialQuantity);
        Option option = product.getOptions().get(0);

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    productService.subtractOptionQuantity(product.getId(), option.getId(), subtractAmount);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    failCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        // then
        Product updatedProduct = productRepository.findByIdWithOptions(product.getId()).get();
        Option updatedOption = updatedProduct.getOptionById(option.getId());

        assertThat(updatedOption.getQuantity()).isEqualTo(0L);
        assertThat(successCount.get()).isEqualTo(1);
        assertThat(failCount.get()).isEqualTo(9);
    }

    private Product createProduct(String categoryName, String productName, int productPrice, String productUrl, String optionName, Long optionQuantity) {
        Category category = categoryRepository.save(new Category(categoryName));
        Product product = new Product(productName, productPrice, productUrl, category);
        Option option = new Option(optionName, optionQuantity);
        product.addOption(option);
        return productRepository.save(product);
    }

}
