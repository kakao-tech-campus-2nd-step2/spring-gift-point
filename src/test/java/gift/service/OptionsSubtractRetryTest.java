package gift.service;


import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import gift.exception.option.FailedRetryException;
import gift.model.Category;
import gift.model.Options;
import gift.model.Product;
import gift.repository.OptionsRepository;
import gift.repository.ProductRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Recover;

@SpringBootTest
@EnableRetry
public class OptionsSubtractRetryTest {

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private OptionsRepository optionsRepository;

    @Autowired
    private OptionsService optionsService;

    private Product savedProduct;
    private Options savedOptions;
    private static final int AVAILABLE_COUNTS = 1;
    private static final int CONCURRENT_SUB_COUNT = 1;
    private static final int CLIENT_COUNT = 2;

    @BeforeEach
    void setUp() {
        Category savedCategory = new Category(1L, "카테고리");
        savedProduct = new Product(1L, "상품", 1000, "http://a.com", savedCategory);
        savedOptions = new Options(1L, "옵션", AVAILABLE_COUNTS, savedProduct);
    }


    @DisplayName("낙관적 락에서 재시도 횟수를 초과하여 요청 처리 실패 테스트")
    @Test
    void failConcurrencyControllingWithOptimisticLock() {
        //given
        given(productRepository.findById(any(Long.class)))
            .willReturn(Optional.of(savedProduct));
        doThrow(ObjectOptimisticLockingFailureException.class)
            .when(optionsRepository).findByIdForUpdate(any(Long.class));

        //when //then
        assertThrows(FailedRetryException.class, () -> {
            optionsService.subtractQuantity(savedOptions.getId(),
                CONCURRENT_SUB_COUNT, savedProduct.getId());
        });

        verify(productRepository, times(100)).findById(anyLong());
        verify(optionsRepository, times(100)).findByIdForUpdate(anyLong());
    }


}
