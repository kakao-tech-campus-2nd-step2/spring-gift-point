package gift.service;

import gift.common.exception.DuplicateDataException;
import gift.common.exception.EntityNotFoundException;
import gift.controller.dto.request.WishRequest;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;
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
class WishServiceTest {

    @InjectMocks
    private WishService wishService;

    @Mock
    private WishRepository wishRepository;
    @Mock
    private ProductRepository productRepository;

    @Test
    @DisplayName("Wish 저장 테스트[실패] - 존재하지않는 Product")
    void checkProductId() {
        // given
        Long productId = 1L;
        int productCount = 11;
        Long memberId = 1L;
        var request = new WishRequest.CreateWish(productId);
        given(productRepository.existsById(eq(productId)))
                .willReturn(false);

        // when
        // then
        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> wishService.save(request, productCount, memberId));
    }

    @Test
    @DisplayName("Wish 저장 테스트[실패] - Wish 중복")
    void checkDuplicateWish() {
        // given
        Long productId = 1L;
        int productCount = 11;
        Long memberId = 1L;
        var request = new WishRequest.CreateWish(productId);
        given(productRepository.existsById(eq(productId)))
                .willReturn(true);
        given(wishRepository.existsByProductIdAndMemberId(eq(productId), eq(memberId)))
                .willReturn(true);

        // when
        // then
        assertThatExceptionOfType(DuplicateDataException.class)
                .isThrownBy(() -> wishService.save(request, productCount, memberId));
    }
}