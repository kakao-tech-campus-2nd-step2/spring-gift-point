package gift.service;

import gift.controller.dto.KakaoApiDTO;
import gift.domain.*;
import gift.repository.*;
import gift.utils.ExternalApiService;
import gift.utils.JwtTokenProvider;
import gift.utils.error.OptionNotFoundException;
import gift.utils.error.TokenAuthException;
import gift.utils.error.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class KakaoApiServiceTest {

    @Mock private OptionRepository optionRepository;
    @Mock private TokenRepository tokenRepository;
    @Mock private UserInfoRepository userInfoRepository;
    @Mock private OrderRepository orderRepository;
    @Mock private WishRepository wishRepository;
    @Mock private ExternalApiService externalApiService;
    @Mock private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private KakaoApiService kakaoApiService;

    private TestFixture fixture;

    @BeforeEach
    void setUp() {
        fixture = new TestFixture();
    }

    @Test
    @DisplayName("카카오 주문 성공")
    void kakaoOrder_Success() {
        // Given
        fixture.givenValidOrderRequest()
            .andValidOption()
            .andValidToken()
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
    @DisplayName("일반 주문 성공")
    void Order_Success() {
        // Given
        fixture.givenValidOrderRequest()
            .andValidOption()
            .andValidUser();

        // When
        KakaoApiDTO.KakaoOrderResponse response = kakaoApiService.Order(fixture.orderRequest, fixture.jwtToken);

        // Then
        assertNotNull(response);
        assertEquals(fixture.optionId, response.optionId());
        assertEquals(fixture.quantity, response.quantity());
        assertEquals(fixture.message, response.message());

        verify(orderRepository).save(any(Order.class));
        verify(wishRepository).findByUserInfoIdAndProductId(fixture.userInfo.getId(), fixture.product.getId());
        verify(wishRepository, never()).delete(any());

        assertEquals(fixture.initialQuantity - fixture.quantity, fixture.option.getQuantity());
        assertEquals(fixture.initialPoint - fixture.point + (int)(fixture.totalPrice * 0.01), fixture.userInfo.getPoint());
    }

    @Test
    @DisplayName("옵션을 찾을 수 없는 경우")
    void kakaoOrder_OptionNotFound() {
        // Given
        fixture.givenValidOrderRequest()
            .givenInvalidOption();

        // When & Then
        assertThrows(OptionNotFoundException.class, () ->
            kakaoApiService.kakaoOrder(fixture.orderRequest, fixture.jwtToken)
        );
    }

    @Test
    @DisplayName("토큰을 찾을 수 없는 경우")
    void kakaoOrder_TokenNotFound() {
        // Given
        fixture.givenValidOrderRequest()
            .andValidOption()
            .andInvalidToken();

        // When & Then
        assertThrows(TokenAuthException.class, () ->
            kakaoApiService.kakaoOrder(fixture.orderRequest, fixture.jwtToken)
        );
    }

    @Test
    @DisplayName("유저를 찾을 수 없는 경우")
    void kakaoOrder_UserNotFound() {
        // Given
        fixture.givenValidOrderRequest()
            .andValidOption()
            .andValidToken()
            .andInvalidUser();

        // When & Then
        assertThrows(UserNotFoundException.class, () ->
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
        int point = 100;
        int initialPoint = 1000;
        int totalPrice = 10000;

        KakaoApiDTO.KakaoOrderRequest orderRequest;
        Product product;
        Option option;
        Token token;
        UserInfo userInfo;

        TestFixture givenValidOrderRequest() {
            this.orderRequest = new KakaoApiDTO.KakaoOrderRequest(optionId, quantity, point,message);
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
            when(option.getTotalPrice(quantity, point)).thenReturn(totalPrice);
            return this;
        }

        TestFixture andValidToken() {
            token = new Token();
            token.setEmail("test@example.com");
            token.updateAccesstoken(accessToken);

            when(jwtTokenProvider.getEmailFromToken(jwtToken)).thenReturn("test@example.com");
            when(tokenRepository.findByEmail("test@example.com")).thenReturn(Optional.of(token));
            return this;
        }

        TestFixture andValidUser() {
            userInfo = new UserInfo();
            userInfo.setId(1L);
            userInfo.setEmail("test@example.com");
            userInfo.setPoint(initialPoint);

            when(userInfoRepository.findByEmail("test@example.com")).thenReturn(Optional.of(userInfo));
            return this;
        }

        TestFixture andNoExistingWish() {
            when(wishRepository.findByUserInfoIdAndProductId(userInfo.getId(), product.getId())).thenReturn(Optional.empty());
            return this;
        }

        TestFixture givenInvalidOption() {
            when(optionRepository.findById(anyLong())).thenReturn(Optional.empty());
            return this;
        }

        TestFixture andInvalidToken() {
            when(jwtTokenProvider.getEmailFromToken(jwtToken)).thenReturn("test@example.com");
            when(tokenRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
            return this;
        }

        TestFixture andInvalidUser() {
            when(userInfoRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
            return this;
        }
    }
}
