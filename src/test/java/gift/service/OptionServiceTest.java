package gift.service;

import static gift.util.constants.OptionConstants.INSUFFICIENT_QUANTITY;
import static gift.util.constants.OptionConstants.OPTION_NAME_DUPLICATE;
import static gift.util.constants.OptionConstants.OPTION_NOT_FOUND;
import static gift.util.constants.OptionConstants.OPTION_REQUIRED;
import static gift.util.constants.ProductConstants.PRODUCT_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gift.dto.option.OptionCreateRequest;
import gift.dto.option.OptionResponse;
import gift.dto.option.OptionUpdateRequest;
import gift.dto.product.ProductResponse;
import gift.exception.option.OptionNotFoundException;
import gift.exception.product.ProductNotFoundException;
import gift.model.Category;
import gift.model.Option;
import gift.model.Product;
import gift.repository.OptionRepository;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.dao.PessimisticLockingFailureException;

public class OptionServiceTest {

    private OptionRepository optionRepository;
    private ProductService productService;
    private OptionService optionService;

    @BeforeEach
    public void setUp() {
        optionRepository = Mockito.mock(OptionRepository.class);
        productService = Mockito.mock(ProductService.class);
        optionService = new OptionService(optionRepository, productService);
    }

    @Test
    @DisplayName("상품 ID로 옵션 조회")
    public void testGetOptionsByProductId() {
        Category category = new Category("Category", "#000000", "imageUrl", "description");
        Product product = new Product(1L, "Product", 100, "imageUrl", category);
        Option option = new Option(1L, "Option1", 100, product);

        when(optionRepository.findByProductId(1L)).thenReturn(List.of(option));

        List<OptionResponse> options = optionService.getOptionsByProductId(1L);
        assertEquals(1, options.size());
        assertEquals("Option1", options.get(0).name());
    }

    @Test
    @DisplayName("옵션 ID로 조회")
    public void testGetOptionById() {
        Category category = new Category("Category", "#000000", "imageUrl", "description");
        Product product = new Product(1L, "Product", 100, "imageUrl", category);
        Option option = new Option(1L, "Option1", 100, product);

        when(optionRepository.findById(1L)).thenReturn(Optional.of(option));

        OptionResponse optionResponse = optionService.getOptionById(1L, 1L);
        assertEquals("Option1", optionResponse.name());
    }

    @Test
    @DisplayName("존재하지 않는 옵션 ID로 조회")
    public void testGetOptionByIdNotFound() {
        when(optionRepository.findById(1L)).thenReturn(Optional.empty());

        OptionNotFoundException exception = assertThrows(OptionNotFoundException.class, () -> {
            optionService.getOptionById(1L, 1L);
        });

        assertEquals(OPTION_NOT_FOUND + 1, exception.getMessage());
    }

    @Test
    @DisplayName("상품에 옵션 추가")
    public void testAddOptionToProduct() {
        Category category = new Category("Category", "#000000", "imageUrl", "description");
        ProductResponse productResponse = new ProductResponse(
            1L,
            "Product",
            100,
            "imageUrl",
            category.getId(),
            category.getName()
        );
        Option option = new Option(
            1L,
            "Option1",
            100,
            new Product(
                productResponse.id(),
                productResponse.name(),
                productResponse.price(),
                productResponse.imageUrl(),
                category
            )
        );
        OptionCreateRequest optionCreateRequest = new OptionCreateRequest("Option1", 100);

        when(productService.getProductById(1L)).thenReturn(productResponse);
        when(optionRepository.save(any(Option.class))).thenReturn(option);

        OptionResponse createdOption = optionService.addOptionToProduct(1L, optionCreateRequest);
        assertEquals("Option1", createdOption.name());
    }

    @Test
    @DisplayName("존재하지 않는 상품에 옵션 추가")
    public void testAddOptionToProductNotFound() {
        OptionCreateRequest optionCreateRequest = new OptionCreateRequest("Option1", 100);

        when(productService.getProductById(1L)).thenThrow(new ProductNotFoundException(
            PRODUCT_NOT_FOUND + 1));

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
            optionService.addOptionToProduct(1L, optionCreateRequest);
        });

        assertEquals(PRODUCT_NOT_FOUND + 1, exception.getMessage());
    }

    @Test
    @DisplayName("중복된 옵션 이름 추가")
    public void testAddOptionWithDuplicateName() {
        Category category = new Category("Category", "#000000", "imageUrl", "description");
        ProductResponse productResponse = new ProductResponse(
            1L,
            "Product",
            100,
            "imageUrl",
            category.getId(),
            category.getName()
        );
        Option existingOption = new Option(
            1L,
            "Option1",
            100,
            new Product(
                productResponse.id(),
                productResponse.name(),
                productResponse.price(),
                productResponse.imageUrl(),
                category
            )
        );
        OptionCreateRequest optionCreateRequest = new OptionCreateRequest("Option1", 200);

        when(productService.getProductById(1L)).thenReturn(productResponse);
        when(optionRepository.findByProductId(1L)).thenReturn(List.of(existingOption));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            optionService.addOptionToProduct(1L, optionCreateRequest);
        });

        assertEquals(OPTION_NAME_DUPLICATE, exception.getMessage());
    }

    @Test
    @DisplayName("옵션 업데이트")
    public void testUpdateOption() {
        Category category = new Category("Category", "#000000", "imageUrl", "description");
        ProductResponse productResponse = new ProductResponse(
            1L,
            "Product",
            100,
            "imageUrl",
            category.getId(),
            category.getName()
        );
        Option existingOption = new Option(
            1L,
            "Old Option",
            100,
            new Product(
                productResponse.id(),
                productResponse.name(),
                productResponse.price(),
                productResponse.imageUrl(),
                category
            )
        );
        Option updatedOption = new Option(
            1L,
            "Updated Option",
            200,
            new Product(
                productResponse.id(),
                productResponse.name(),
                productResponse.price(),
                productResponse.imageUrl(),
                category
            )
        );
        OptionUpdateRequest optionUpdateRequest = new OptionUpdateRequest("Updated Option", 200);

        when(productService.getProductById(1L)).thenReturn(productResponse);
        when(optionRepository.findById(1L)).thenReturn(Optional.of(existingOption));
        when(optionRepository.save(any(Option.class))).thenReturn(updatedOption);

        OptionResponse result = optionService.updateOption(1L, 1L, optionUpdateRequest);
        assertEquals("Updated Option", result.name());
        assertEquals(200, result.quantity());
    }

    @Test
    @DisplayName("존재하지 않는 옵션 ID로 업데이트")
    public void testUpdateOptionNotFound() {
        Category category = new Category("Category", "#000000", "imageUrl", "description");
        ProductResponse productResponse = new ProductResponse(
            1L,
            "Product",
            100,
            "imageUrl",
            category.getId(),
            category.getName()
        );
        OptionUpdateRequest optionUpdateRequest = new OptionUpdateRequest("Updated Option", 200);

        when(productService.getProductById(1L)).thenReturn(productResponse);
        when(optionRepository.findById(1L)).thenReturn(Optional.empty());

        OptionNotFoundException exception = assertThrows(OptionNotFoundException.class, () -> {
            optionService.updateOption(1L, 1L, optionUpdateRequest);
        });

        assertEquals(OPTION_NOT_FOUND + 1, exception.getMessage());
    }

    @Test
    @DisplayName("옵션 삭제")
    public void testDeleteOption() {
        Category category = new Category("Category", "#000000", "imageUrl", "description");
        ProductResponse productResponse = new ProductResponse(
            1L,
            "Product",
            100,
            "imageUrl",
            category.getId(),
            category.getName()
        );
        Option option1 = new Option(
            1L,
            "Option1",
            100,
            new Product(
                productResponse.id(),
                productResponse.name(),
                productResponse.price(),
                productResponse.imageUrl(),
                category
            )
        );
        Option option2 = new Option(
            2L,
            "Option2",
            100,
            new Product(
                productResponse.id(),
                productResponse.name(),
                productResponse.price(),
                productResponse.imageUrl(),
                category
            )
        );

        when(productService.getProductById(1L)).thenReturn(productResponse);
        when(optionRepository.findById(1L)).thenReturn(Optional.of(option1));
        when(optionRepository.findById(2L)).thenReturn(Optional.of(option2));
        when(optionRepository.findByProductId(1L)).thenReturn(List.of(option1, option2));
        doNothing().when(optionRepository).delete(option1);

        optionService.deleteOption(1L, 1L);
        verify(optionRepository, times(1)).delete(option1);
    }

    @Test
    @DisplayName("존재하지 않는 옵션 ID로 삭제")
    public void testDeleteOptionNotFound() {
        Category category = new Category("Category", "#000000", "imageUrl", "description");
        ProductResponse productResponse = new ProductResponse(
            1L,
            "Product",
            100,
            "imageUrl",
            category.getId(),
            category.getName()
        );

        when(productService.getProductById(1L)).thenReturn(productResponse);
        when(optionRepository.findById(1L)).thenReturn(Optional.empty());

        OptionNotFoundException exception = assertThrows(OptionNotFoundException.class, () -> {
            optionService.deleteOption(1L, 1L);
        });

        assertEquals(OPTION_NOT_FOUND + 1, exception.getMessage());
    }

    @Test
    @DisplayName("마지막 옵션 삭제")
    public void testDeleteLastOption() {
        Category category = new Category("Category", "#000000", "imageUrl", "description");
        ProductResponse productResponse = new ProductResponse(
            1L,
            "Product",
            100,
            "imageUrl",
            category.getId(),
            category.getName()
        );
        Option option = new Option(
            1L,
            "Option1",
            100,
            new Product(
                productResponse.id(),
                productResponse.name(),
                productResponse.price(),
                productResponse.imageUrl(),
                category
            )
        );

        when(productService.getProductById(1L)).thenReturn(productResponse);
        when(optionRepository.findById(1L)).thenReturn(Optional.of(option));
        when(optionRepository.findByProductId(1L)).thenReturn(List.of(option));
        doThrow(new IllegalArgumentException(OPTION_REQUIRED)).when(optionRepository)
            .delete(option);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            optionService.deleteOption(1L, 1L);
        });

        assertEquals(OPTION_REQUIRED, exception.getMessage());
    }

    @Test
    @DisplayName("옵션 수량 차감")
    public void testSubtractOptionQuantity() {
        Category category = new Category("Category", "#000000", "imageUrl", "description");
        Product product = new Product(1L, "Product", 100, "imageUrl", category);
        Option option = new Option(1L, "Option1", 100, product);

        when(optionRepository.findByIdAndProductIdWithLock(1L, 1L)).thenReturn(Optional.of(option));
        when(optionRepository.save(any(Option.class))).thenReturn(option);

        optionService.subtractOptionQuantity(1L, 1L, 10);

        assertEquals(90, option.getQuantity());
    }

    @Test
    @DisplayName("옵션 수량 차감 - 옵션 ID 불일치")
    public void testSubtractOptionQuantityOptionIdMismatch() {
        when(optionRepository.findById(1L)).thenReturn(Optional.empty());

        OptionNotFoundException exception = assertThrows(OptionNotFoundException.class, () -> {
            optionService.subtractOptionQuantity(1L, 1L, 10);
        });

        assertEquals(OPTION_NOT_FOUND + 1, exception.getMessage());
    }

    @Test
    @DisplayName("옵션 수량 차감 - 수량 부족")
    public void testSubtractOptionQuantityInsufficientQuantity() {
        Category category = new Category("Category", "#000000", "imageUrl", "description");
        Product product = new Product(1L, "Product", 100, "imageUrl", category);
        Option option = new Option(1L, "Option1", 5, product);

        when(optionRepository.findByIdAndProductIdWithLock(1L, 1L)).thenReturn(Optional.of(option));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            optionService.subtractOptionQuantity(1L, 1L, 10);
        });

        assertEquals(INSUFFICIENT_QUANTITY + 1, exception.getMessage());
    }

    @Test
    @DisplayName("동시성 문제 - 옵션 수량 차감 동시 요청 처리")
    public void testConcurrentSubtractOptionQuantity() throws InterruptedException {
        Category category = new Category("Category", "#000000", "imageUrl", "description");
        Product product = new Product(1L, "Product", 100, "imageUrl", category);
        Option option = new Option(1L, "Option1", 100, product);

        when(optionRepository.findByIdAndProductIdWithLock(1L, 1L)).thenReturn(Optional.of(option));
        when(optionRepository.save(any(Option.class))).thenReturn(option);

        int numberOfThreads = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    optionService.subtractOptionQuantity(1L, 1L, 10);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        assertEquals(0, option.getQuantity());
        executorService.shutdown();
    }

    @Test
    @DisplayName("동시성 문제 - 수량 부족으로 예외 발생")
    public void testConcurrentSubtractOptionQuantityInsufficientQuantity()
        throws InterruptedException {
        Category category = new Category("Category", "#000000", "imageUrl", "description");
        Product product = new Product(1L, "Product", 100, "imageUrl", category);
        Option option = new Option(1L, "Option1", 50, product);

        when(optionRepository.findByIdAndProductIdWithLock(1L, 1L)).thenReturn(Optional.of(option));
        when(optionRepository.save(any(Option.class))).thenReturn(option);

        int numberOfThreads = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    optionService.subtractOptionQuantity(1L, 1L, 10);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        assertTrue(option.getQuantity() >= 0);
        executorService.shutdown();
    }

    @Test
    @DisplayName("동시성 문제 - 중간에 수량 부족으로 예외 발생")
    public void testConcurrentSubtractOptionQuantityMixed() throws InterruptedException {
        Category category = new Category("Category", "#000000", "imageUrl", "description");
        Product product = new Product(1L, "Product", 100, "imageUrl", category);
        Option option = new Option(1L, "Option1", 60, product);

        when(optionRepository.findByIdAndProductIdWithLock(1L, 1L)).thenReturn(Optional.of(option));
        when(optionRepository.save(any(Option.class))).thenReturn(option);

        int numberOfThreads = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    optionService.subtractOptionQuantity(1L, 1L, 10);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        assertTrue(option.getQuantity() >= 0);
        executorService.shutdown();
    }

    @Test
    @DisplayName("동시성 문제 - 예외 발생 후 재시도")
    public void testConcurrentSubtractOptionQuantityWithRetry() throws InterruptedException {
        Category category = new Category("Category", "#000000", "imageUrl", "description");
        Product product = new Product(1L, "Product", 100, "imageUrl", category);
        Option option = new Option(1L, "Option1", 100, product);

        when(optionRepository.findByIdAndProductIdWithLock(1L, 1L)).thenReturn(Optional.of(option));
        when(optionRepository.save(any(Option.class))).thenReturn(option);

        int numberOfThreads = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                boolean success = false;
                while (!success) {
                    try {
                        optionService.subtractOptionQuantity(1L, 1L, 10);
                        success = true;
                    } catch (PessimisticLockingFailureException e) {
                        System.out.println("재시도: " + e.getMessage());
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        success = true;
                    }
                }
                latch.countDown();
            });
        }

        latch.await();

        assertEquals(0, option.getQuantity());
        executorService.shutdown();
    }

    @Test
    @DisplayName("동시성 문제 - PessimisticLockingFailureException 발생")
    public void testPessimisticLockingFailureException() throws InterruptedException {
        Category category = new Category("Category", "#000000", "imageUrl", "description");
        Product product = new Product(1L, "Product", 100, "imageUrl", category);
        Option option = new Option(1L, "Option1", 100, product);

        when(optionRepository.findByIdAndProductIdWithLock(1L, 1L)).thenAnswer(invocation -> {
            throw new PessimisticLockingFailureException("Locking failed");
        });

        int numberOfThreads = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    optionService.subtractOptionQuantity(1L, 1L, 10);
                } catch (PessimisticLockingFailureException e) {
                    System.out.println("Locking 실패 " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        assertEquals(100, option.getQuantity());
        executorService.shutdown();
    }
}
