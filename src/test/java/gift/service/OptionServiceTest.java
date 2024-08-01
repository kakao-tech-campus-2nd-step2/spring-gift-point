package gift.service;

import gift.domain.OptionDTO;
import gift.entity.Category;
import gift.entity.Options;
import gift.entity.Option;
import gift.entity.Product;
import gift.repository.OptionRepository;
import gift.repository.OptionsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OptionServiceTest {
    @Mock
    private OptionsRepository optionsRepository;

    @Mock
    private OptionRepository optionRepository;

    @InjectMocks
    private OptionService optionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByProduct_Id() {
        // Given
        int productId = 1;
        Category testCategory = new Category("test", "test", "test", "test");
        Product product = new Product(testCategory,1, "test", "testURL");
        Options options = new Options(product, new ArrayList<>());
        when(optionsRepository.findByProduct_Id(productId)).thenReturn(Optional.of(options));

        // When
        Optional<Options> result = optionService.findByProduct_Id(productId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(options, result.get());
        verify(optionsRepository).findByProduct_Id(productId);
    }

    @Test
    void testAddOption() {
        // Given
        int productId = 1;
        Category testCategory = new Category("test", "test", "test", "test");
        Product product = new Product(testCategory,1, "test", "testURL");
        OptionDTO optionDTO = new OptionDTO("Option 1", 100);
        Option savedOption = new Option(optionDTO);
        Options options = new Options(product, new ArrayList<>());

        when(optionRepository.save(any(Option.class))).thenReturn(savedOption);
        when(optionsRepository.findByProduct_Id(productId)).thenReturn(Optional.of(options));
        when(optionsRepository.save(any(Options.class))).thenReturn(options);

        // When
        Option result = optionService.addOption(productId, optionDTO);

        // Then
        assertNotNull(result);
        assertEquals(savedOption, result);
        verify(optionRepository).save(any(Option.class));
        verify(optionsRepository).findByProduct_Id(productId);
        verify(optionsRepository).save(any(Options.class));
    }

    @Test
    void testAddOptionProductNotFound() {
        // Given
        int productId = 1;
        OptionDTO optionDTO = new OptionDTO("Option 1", 100);

        when(optionsRepository.findByProduct_Id(productId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoSuchElementException.class, () -> optionService.addOption(productId, optionDTO));
    }

    @Test
    void testUpdateOption() {
        // Given
        int productId = 1;
        int optionId = 1;
        Category testCategory = new Category("test", "test", "test", "test");
        Product product = new Product(testCategory,1, "test", "testURL");
        OptionDTO optionDTO = new OptionDTO("Updated Option", 150);
        Option updatedOption = new Option(optionId, optionDTO);
        Options options = new Options(product, new ArrayList<>());

        when(optionRepository.save(any(Option.class))).thenReturn(updatedOption);
        when(optionsRepository.findByProduct_Id(productId)).thenReturn(Optional.of(options));
        when(optionsRepository.save(any(Options.class))).thenReturn(options);

        // When
        Option result = optionService.updateOption(productId, optionId, optionDTO);

        // Then
        assertNotNull(result);
        assertEquals(updatedOption, result);
        verify(optionRepository).save(any(Option.class));
        verify(optionsRepository).findByProduct_Id(productId);
        verify(optionsRepository).save(any(Options.class));
    }

    @Test
    void testUpdateOptionProductNotFound() {
        // Given
        int productId = 1;
        int optionId = 1;
        OptionDTO optionDTO = new OptionDTO("Updated Option", 150);

        // When
        when(optionsRepository.findByProduct_Id(productId)).thenReturn(Optional.empty());

        // Then
        assertThrows(NoSuchElementException.class, () -> optionService.updateOption(productId, optionId, optionDTO));
    }

    @Test
    void testDeleteOption() {
        // Given
        int productId = 1;
        int optionId = 1;
        Category testCategory = new Category("test", "test", "test", "test");
        Product product = new Product(testCategory,1, "test", "testURL");
        Options options = new Options(product, new ArrayList<>());

        when(optionsRepository.findByProduct_Id(productId)).thenReturn(Optional.of(options));
        when(optionsRepository.save(any(Options.class))).thenReturn(options);

        // When
        assertDoesNotThrow(() -> optionService.deleteOption(productId, optionId));

        // Then
        verify(optionsRepository).findByProduct_Id(productId);
        verify(optionsRepository).save(any(Options.class));
    }

    @Test
    void testDeleteOptionProductNotFound() {
        // Given
        int productId = 1;
        int optionId = 1;

        // When
        when(optionsRepository.findByProduct_Id(productId)).thenReturn(Optional.empty());

        // Then
        assertThrows(NoSuchElementException.class, () -> optionService.deleteOption(productId, optionId));
    }

    @Test
    void testDeductOptionQuantity() {
        // Given
        int productId = 1;
        int optionId = 1;
        Category testCategory = new Category("test", "test", "test", "test");
        Product product = new Product(testCategory,1, "test", "testURL");
        OptionDTO optionDTO = new OptionDTO("Updated Option", 150);
        Option updatedOption = new Option(optionId, optionDTO);
        Options options = new Options(product, new ArrayList<>());

        // When
        when(optionRepository.save(any(Option.class))).thenReturn(updatedOption);
        when(optionsRepository.findByProduct_Id(productId)).thenReturn(Optional.of(options));
        when(optionsRepository.save(any(Options.class))).thenReturn(options);
        when(optionRepository.searchQuantityById(optionId)).thenReturn(updatedOption.getQuantity());
        int currentQuantity = optionService.deductQuantity(productId, optionId);

        // Then
        assertNotNull(currentQuantity);
        assertEquals(updatedOption.getQuantity() - 1, currentQuantity);
    }

    @Test
    void testDeductOptionQuantityWhenQuantityisZero() {
        // Given
        int productId = 1;
        int optionId = 1;
        Category testCategory = new Category("test", "test", "test", "test");
        Product product = new Product(testCategory,1, "test", "testURL");
        OptionDTO optionDTO = new OptionDTO("Updated Option", 0);
        Option updatedOption = new Option(optionId, optionDTO);
        Options options = new Options(product, new ArrayList<>());

        // When
        when(optionRepository.save(any(Option.class))).thenReturn(updatedOption);
        when(optionsRepository.findByProduct_Id(productId)).thenReturn(Optional.of(options));
        when(optionsRepository.save(any(Options.class))).thenReturn(options);
        when(optionRepository.searchQuantityById(optionId)).thenReturn(updatedOption.getQuantity());

        // Then
        assertThrows(IllegalArgumentException.class, () -> optionService.deductQuantity(productId, optionId));
    }

    @Test
    void testDeductOptionQuantityAtSameTime() throws InterruptedException {
        // Given
        int numThreads = 10;
        int initialStock = 5;

        int productId = 1;
        int optionId = 1;
        Category testCategory = new Category("test", "test", "test", "test");
        Product product = new Product(testCategory,1, "test", "testURL");
        Options options = new Options(product, new ArrayList<>());

        CountDownLatch doneSignal = new CountDownLatch(numThreads);
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        // When
        when(optionsRepository.findByProduct_Id(productId)).thenReturn(Optional.of(options));
        when(optionsRepository.save(any(Options.class))).thenReturn(options);

        AtomicInteger currentQuantity = new AtomicInteger(initialStock);

        when(optionRepository.searchQuantityById(optionId)).thenAnswer(invocation -> currentQuantity.get());
        when(optionRepository.updateQuantityById(eq(optionId), anyInt())).thenAnswer(invocation -> currentQuantity.getAndDecrement());

        for (int i = 0; i < numThreads; i++) {
            executorService.execute(() -> {
                try {
                    optionService.deductQuantity(productId, optionId);
                    successCount.getAndIncrement();
                } catch (Exception e) {
                    failCount.getAndIncrement();
                } finally {
                    doneSignal.countDown();
                }
            });
        }
        doneSignal.await();
        executorService.shutdown();

        //Then
        assertAll(
                () -> assertThat(successCount.get()).isEqualTo(5),
                () -> assertThat(failCount.get()).isEqualTo(5),
                () -> assertThat(currentQuantity.get()).isEqualTo(0)
        );
    }
}