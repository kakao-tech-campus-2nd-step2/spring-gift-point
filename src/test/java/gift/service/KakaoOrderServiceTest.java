package gift.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.administrator.option.OptionDTO;
import gift.administrator.option.OptionService;
import gift.administrator.product.ProductDTO;
import gift.administrator.product.ProductService;
import gift.error.KakaoOrderException;
import gift.token.TokenService;
import gift.users.kakao.KakaoApiResponse;
import gift.users.kakao.KakaoOrderDTO;
import gift.users.kakao.KakaoOrderService;
import gift.users.kakao.KakaoProperties;
import gift.users.wishlist.WishListService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class KakaoOrderServiceTest {

    private MockRestServiceServer server;
    private KakaoOrderService kakaoOrderService;
    private TokenService tokenService = mock(TokenService.class);
    private ProductService productService = mock(ProductService.class);
    private OptionService optionService = mock(OptionService.class);
    private RestClient.Builder restClientBuilder;
    @Autowired
    private KakaoProperties kakaoProperties;
    private WishListService wishListService = mock(WishListService.class);

    @BeforeEach
    void beforeEach() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(10000);
        restClientBuilder = RestClient.builder()
            .requestFactory(factory)
            .defaultHeaders(headers -> {
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            });
        server = MockRestServiceServer.bindTo(restClientBuilder).build();
        kakaoOrderService = new KakaoOrderService(tokenService, productService,
            optionService, restClientBuilder, kakaoProperties, wishListService);
    }

    @Test
    @DisplayName("카카오 주문 성공")
    void kakaoOrder() throws JsonProcessingException {
        //given
        given(productService.existsByProductId(anyLong())).willReturn(false);
        OptionDTO optionDTO = new OptionDTO("XL", 3, 1L);
        given(optionService.findOptionById(anyLong())).willReturn(optionDTO);

        doNothing().when(optionService)
            .subtractOptionQuantityErrorIfNotPossible(anyLong(), anyInt());
        given(tokenService.findToken(anyLong(), anyString())).willReturn("access-token");

        ProductDTO productDTO = new ProductDTO(1L, "신라면", 3000, "imageUrl", 1L, null);
        given(productService.getProductById(anyLong())).willReturn(productDTO);
        given(optionService.findOptionById(anyLong())).willReturn(optionDTO);
        KakaoApiResponse kakaoApiResponse = new KakaoApiResponse(0);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(kakaoApiResponse);

        server.expect(requestTo(kakaoProperties.sendToMeUrl()))
            .andExpect(method(POST))
            .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        given(optionService.subtractOptionQuantity(anyLong(), anyInt())).willReturn(null);
        doNothing().when(wishListService)
            .findOrderAndDeleteIfExists(anyLong(), anyLong(), anyLong());
        KakaoOrderDTO kakaoOrderDTO = new KakaoOrderDTO(1L, 1L,
            3, null, "안녕하세요");
        KakaoOrderDTO expected = new KakaoOrderDTO(1L, 1L,
            3, "2024-10-23", "안녕하세요");

        //when
        KakaoOrderDTO result = kakaoOrderService.kakaoOrder(1L, kakaoOrderDTO, "2024-10-23");

        //then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("카카오 주문 시 없는 상품 주문하면 Exception 발생")
    void kakaoOrderFailNotExistingProduct() {
        //given
        given(productService.existsByProductId(anyLong())).willReturn(true);
        KakaoOrderDTO kakaoOrderDTO = new KakaoOrderDTO(1L, 1L,
            3, null, "안녕하세요");

        //when

        //then
        assertThatThrownBy(() -> kakaoOrderService.kakaoOrder(1L, kakaoOrderDTO, "2024-10-23"))
            .isInstanceOf(KakaoOrderException.class).hasMessageContaining("없는 상품입니다.");
    }

    @Test
    @DisplayName("카카오 주문 시 상품에 없는 옵션 주문시 exception 발생")
    void kakaoOrderFailNotExistingOptionInProduct() {
        //given
        given(productService.existsByProductId(anyLong())).willReturn(false);

        OptionDTO optionDTO = new OptionDTO("XL", 3, 1L);
        given(optionService.findOptionById(anyLong())).willReturn(optionDTO);

        KakaoOrderDTO kakaoOrderDTO = new KakaoOrderDTO(2L, 1L,
            3, null, "안녕하세요");

        //when

        //then
        assertThatThrownBy(() -> kakaoOrderService.kakaoOrder(1L, kakaoOrderDTO, "2024-10-23"))
            .isInstanceOf(KakaoOrderException.class)
            .hasMessageContaining(kakaoOrderDTO.productId() +
                " 상품에 " + kakaoOrderDTO.optionId() + " 옵션이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("카카오 주문 시 옵션 재고가 부족함")
    void kakaoOrderFailCannotSubtract() {
        //given
        given(productService.existsByProductId(anyLong())).willReturn(false);

        OptionDTO optionDTO = new OptionDTO("XL", 3, 1L);
        given(optionService.findOptionById(anyLong())).willReturn(optionDTO);

        KakaoOrderDTO kakaoOrderDTO = new KakaoOrderDTO(1L, 1L,
            3, null, "안녕하세요");

        doThrow(new IllegalArgumentException("옵션의 재고가 부족합니다."))
            .when(optionService).subtractOptionQuantityErrorIfNotPossible(anyLong(), anyInt());

        //when

        //then
        assertThatThrownBy(() -> kakaoOrderService.kakaoOrder(1L, kakaoOrderDTO, "2024-10-23"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("옵션의 재고가 부족합니다.");
    }
}
