package gift.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import gift.model.categories.Category;
import gift.model.item.Item;
import gift.model.option.Option;
import gift.repository.CategoryRepository;
import gift.repository.ItemRepository;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
public class testSynchronization {

    @Autowired
    private ItemService itemService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ItemRepository itemRepository;

    private final String TEST_NAME = "name";
    private final String TEST_URL = "imgUrl";
    private final int NUM_REF = 50;


    private Long quantity = 100L;
    private Category category = new Category(0L, TEST_NAME, TEST_URL);
    private Long itemId;
    private Long optionId;

    @Rollback(value = false)
    @Transactional
    void setUp() {
        Category savedCategory = categoryRepository.save(category);
        Item item = new Item(TEST_NAME, 2000L, TEST_URL, savedCategory);
        Option option = new Option(TEST_NAME, quantity, item);
        item.addOption(option);
        itemId = itemRepository.save(item).getId();
        optionId = item.getOptions().get(0).getId();
    }

    @DisplayName("재고 감소 동시성 테스트")
    @Test
    void testSynchronousDecreasing() throws InterruptedException {

        setUp();

        ExecutorService service = Executors.newFixedThreadPool(NUM_REF);
        CountDownLatch countDownLatch = new CountDownLatch(NUM_REF);

        for (int i = 0; i < NUM_REF; i++) {
            service.submit(() -> {
                itemService.decreaseOptionQuantity(itemId, optionId, 1L);
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();

        Long result = getQuantity();
        assertThat(result).isEqualTo(quantity - NUM_REF);
    }

    @Transactional
    Long getQuantity() {
        return itemService.getOption(itemId, optionId).getQuantity();
    }

}
