package gift.option.application;

import gift.category.domain.Category;
import gift.category.domain.CategoryRepository;
import gift.exception.type.InvalidOptionQuantityException;
import gift.exception.type.NotFoundException;
import gift.option.application.command.OptionSubtractQuantityCommand;
import gift.option.domain.Option;
import gift.option.domain.OptionRepository;
import gift.product.domain.Product;
import gift.product.domain.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
class OptionServiceIntegrationTest {

    @Autowired
    private OptionService optionService;

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Option option;

    private Product product;

    @BeforeEach
    public void setUp() {
        option = new Option("Option1", 100);
        Category category = new Category("name", "color", "description", "imageUrl");
        product = new Product("Product1", 1000, "image", category);
        option.setProduct(product);

        categoryRepository.save(category);
        productRepository.save(product);
        optionRepository.save(option);
    }

    @AfterEach
    public void tearDown() {
        optionRepository.deleteAll();
        productRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    public void 옵션이_존재할_때_옵션_수량을_차감() {
        // Given
        OptionSubtractQuantityCommand command = new OptionSubtractQuantityCommand(option.getId(), 1);

        // When
        optionService.subtractOptionQuantity(command);

        // Then
        Option updatedOption = optionRepository.findById(option.getId()).orElseThrow();
        assertThat(updatedOption.getQuantity()).isEqualTo(99);
    }

    @Test
    public void 옵션이_존재하지_않을_때_예외() {
        // Given
        OptionSubtractQuantityCommand command = new OptionSubtractQuantityCommand(-1L, 1);

        // When & Then
        assertThatThrownBy(() -> optionService.subtractOptionQuantity(command)).isInstanceOf(NotFoundException.class).hasMessage("해당 옵션이 존재하지 않습니다.");
    }

    @Test
    public void 수량이_1일때_동시_차감_요청() throws Exception {
        // Given
        option.subtractQuantity(99);
        optionRepository.save(option);
        OptionSubtractQuantityCommand command = new OptionSubtractQuantityCommand(option.getId(), 1);

        // When
        ExecutorService executor = Executors.newFixedThreadPool(2);
        Future<?> future1 = executor.submit(() -> {
            optionService.subtractOptionQuantity(command);
        });
        Future<?> future2 = executor.submit(() -> {
            optionService.subtractOptionQuantity(command);
        });

        // Then
        boolean optimisticLockExceptionThrown = false;
        boolean invalidOptionQuantityExceptionThrown = false;

        try {
            future1.get();
        } catch (Exception e) {
            if (e.getCause() instanceof ObjectOptimisticLockingFailureException) {
                optimisticLockExceptionThrown = true;
            } else if (e.getCause() instanceof InvalidOptionQuantityException) {
                invalidOptionQuantityExceptionThrown = true;
            } else {
                throw e;
            }
        }

        try {
            future2.get();
        } catch (Exception e) {
            if (e.getCause() instanceof ObjectOptimisticLockingFailureException) {
                optimisticLockExceptionThrown = true;
            } else if (e.getCause() instanceof InvalidOptionQuantityException) {
                invalidOptionQuantityExceptionThrown = true;
            } else {
                throw e;
            }
        }

        assertThat(optimisticLockExceptionThrown || invalidOptionQuantityExceptionThrown).isTrue();

        Option updatedOption = optionRepository.findById(option.getId()).orElseThrow();
        assertThat(updatedOption.getQuantity()).isEqualTo(0);
    }

}
