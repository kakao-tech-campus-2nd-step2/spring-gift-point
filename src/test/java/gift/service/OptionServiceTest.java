package gift.service;

import gift.dto.OptionRequestDto;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import gift.vo.Category;
import gift.vo.Option;
import gift.vo.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class OptionServiceTest {

    @Mock
    private OptionRepository optionRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private Category category;

    @InjectMocks
    private OptionService optionService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product(1L, category, "테스트용 상품", 10_000, "http://image.png");
    }

    @Test
    @DisplayName("Test for option name is duplicate")
    void optionNameIsDuplicate() {
        // given
        String duplicateName = "[테스트]옵션입니다";
        OptionRequestDto requestAddOption = new OptionRequestDto(null, product.getId(), duplicateName, 10);
        Option existingOption = new Option(1L, product, duplicateName, 15);  // 이미 존재하는 옵션
        List<OptionRequestDto> requestAddOptions = Collections.singletonList(requestAddOption);

        when(productRepository.findById(requestAddOption.productId())).thenReturn(Optional.of(product));
        when(optionRepository.findByNameAndProductId(duplicateName, product.getId()))
                .thenReturn(Optional.of(existingOption));

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            optionService.addOption(requestAddOptions);
        });

        assertEquals("상품 옵션명이 중복입니다. 다른 옵션명으로 변경해주세요.", exception.getMessage());
    }

    @Test
    @DisplayName("Test for option name is not duplicate")
    void optionNameIsNotDuplicate() {
        // given
        String optionName = "[테스트]추가하려는 옵션입니다";
        OptionRequestDto requestAddOption = new OptionRequestDto(null, product.getId(), optionName, 10);
        List<OptionRequestDto> requestAddOptions = Collections.singletonList(requestAddOption);

        when(productRepository.findById(requestAddOption.productId())).thenReturn(Optional.of(product));
        when(optionRepository.findByNameAndProductId(optionName, product.getId()))
                .thenReturn(Optional.empty());

        // when
        optionService.addOption(requestAddOptions);

        // then
        // Verify that save method was not called
        verify(optionRepository, never()).save(any(Option.class));
    }
}
