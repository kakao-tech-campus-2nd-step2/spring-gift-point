package gift.service;

import gift.controller.dto.KakaoApiDTO;
import gift.domain.*;
import gift.repository.*;
import gift.utils.ExternalApiService;
import gift.utils.JwtTokenProvider;
import gift.utils.error.OptionNotFoundException;
import gift.utils.error.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class KakaoApiServiceTest {

    @Mock private OptionRepository optionRepository;
    @Mock private TokenRepository tokenRepository;
    @Mock private UserInfoRepository userInfoRepository;
    @Mock private OrderRepository orderRepository;
    @Mock private ExternalApiService externalApiService;
    @Mock private WishRepository wishRepository;
    @Mock private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private KakaoApiService kakaoApiService;

    private TestFixture fixture;

    @BeforeEach
    void setUp() {
        fixture = new TestFixture();
    }

    @Test
    @DisplayName("주문 성공 상황")
    void kakaoOrder_Success() {
        // Given
        fixture.givenValidOrderRequest()
            .andValidOption()
            .andValidUser()
            .andNoExistingWish();

        // When
        KakaoApiDTO.KakaoOrderResponse response = kakaoApiService.kakaoOrder(fixture.orderRequest, fixture.jwtToken);

        // Then
        assertNotNull(response);
        assertEquals(fixture.optionId, response.optionId());
        assertEquals(fixture.quantity, response.quantity());
        assertEquals(fixture.message, response.message());

        verify(orderRepository).save(any(Order.class));
        verify(externalApiService).postSendMe(any(KakaoApiDTO.KakaoOrderResponse.class), eq(fixture.accessToken));
        verify(wishRepository).findByUserInfoIdAndProductId(fixture.userInfo.getId(), fixture.product.getId());
        verify(wishRepository, never()).delete(any());

        assertEquals(fixture.initialQuantity - fixture.quantity, fixture.option.getQuantity());
    }

    @Test
    @DisplayName("주문 성공 및 위시리스트 삭제 상황")
    void kakaoOrder_SuccessWithWishDelete() {
        // Given
        fixture.givenValidOrderRequest()
            .andValidOption()
            .andValidUser()
            .andExistingWish();

        // When
        KakaoApiDTO.KakaoOrderResponse response = kakaoApiService.kakaoOrder(fixture.orderRequest, fixture.jwtToken);

        // Then
        assertNotNull(response);
        verify(wishRepository).delete(fixture.wish);
    }

    @Test
    @DisplayName("옵션을 찾을 수 없는 상황")
    void kakaoOrder_OptionNotFound() {
        // Given
        fixture.givenValidOrderRequest()
            .givenInvalidOption();

        when(jwtTokenProvider.getEmailFromToken(anyString())).thenReturn("test@example.com");

        // When & Then
        assertThrows(OptionNotFoundException.class, () ->
            kakaoApiService.kakaoOrder(fixture.orderRequest, fixture.jwtToken)
        );
    }

    @Test
    @DisplayName("유저를 찾을 수 없는 상황")
    void kakaoOrder_UserNotFound() {
        // Given
        fixture.givenValidOrderRequest()
            .andValidOption()
            .andInvalidUser();

        // When & Then
        assertThrows(UserNotFoundException.class, () ->
            kakaoApiService.kakaoOrder(fixture.orderRequest, fixture.jwtToken)
        );
    }

    @Test
    @DisplayName("재고가 부족한 상황")
    void kakaoOrder_InsufficientQuantity() {
        // Given
        fixture.givenValidOrderRequest()
            .andInsufficientStock()
            .andValidUser();

        // When & Then
        assertThrows(IllegalStateException.class, () ->
            kakaoApiService.kakaoOrder(fixture.orderRequest, fixture.jwtToken)
        );
    }

    private class TestFixture {
        Long optionId = 1L;
        int quantity = 2;
        String message = "Test message";
        String jwtToken = "jwt_token";
        String accessToken = "access_token";
        int initialQuantity = 10;

        KakaoApiDTO.KakaoOrderRequest orderRequest;
        Product product;
        Option option;
        Token token;
        UserInfo userInfo;
        Wish wish;

        TestFixture givenValidOrderRequest() {
            this.orderRequest = new KakaoApiDTO.KakaoOrderRequest(optionId, quantity, message);
            return this;
        }

        TestFixture andValidOption() {
            product = new Product();
            product.setId(1L);

            option = new Option();
            option.setId(optionId);
            option.setQuantity(initialQuantity);
            option.setProduct(product);

            when(optionRepository.findById(optionId)).thenReturn(Optional.of(option));
            return this;
        }

        TestFixture andInsufficientStock() {
            product = new Product();
            product.setId(1L);

            option = new Option();
            option.setId(optionId);
            option.setQuantity(1); // 요청 수량보다 적은 재고
            option.setProduct(product);

            when(optionRepository.findById(optionId)).thenReturn(Optional.of(option));
            return this;
        }

        TestFixture andValidUser() {
            token = new Token();
            token.setEmail("test@example.com");
            token.updateAccesstoken(accessToken);

            userInfo = new UserInfo();
            userInfo.setId(1L);
            userInfo.setEmail("test@example.com");

            when(jwtTokenProvider.getEmailFromToken(jwtToken)).thenReturn("test@example.com");
            when(tokenRepository.findByEmail("test@example.com")).thenReturn(Optional.of(token));
            when(userInfoRepository.findByEmail("test@example.com")).thenReturn(Optional.of(userInfo));
            return this;
        }

        TestFixture andInvalidUser() {
            when(jwtTokenProvider.getEmailFromToken(jwtToken)).thenReturn("test@example.com");
            when(tokenRepository.findByEmail("test@example.com")).thenReturn(Optional.of(new Token()));
            when(userInfoRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
            return this;
        }

        TestFixture andNoExistingWish() {
            when(wishRepository.findByUserInfoIdAndProductId(userInfo.getId(), product.getId())).thenReturn(Optional.empty());
            return this;
        }

        TestFixture andExistingWish() {
            wish = new Wish();
            when(wishRepository.findByUserInfoIdAndProductId(userInfo.getId(), product.getId())).thenReturn(Optional.of(wish));
            return this;
        }

        TestFixture givenInvalidOption() {
            when(optionRepository.findById(anyLong())).thenReturn(Optional.empty());
            return this;
        }
    }
}
