package gift.service;

import gift.common.exception.EntityNotFoundException;
import gift.controller.dto.request.OptionRequest;
import gift.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ProductServiceTest {
    @InjectMocks
    private ProductService productService;
    @Mock
    private ProductRepository productRepository;

    @Test
    @DisplayName("옵션 저장 테스트[실패] - 잘못된 product productId")
    void save() {
        // given
        String name = "name";
        int quantity = 2;
        Long productId = 1L;
        var request = new OptionRequest.CreateOption(name, quantity, productId);
        given(productRepository.existsById(eq(productId)))
                .willReturn(false);

        // when
        // then
        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(()->productService.addOption(request));
    }
}