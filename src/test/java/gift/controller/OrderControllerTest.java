package gift.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.domain.AuthToken;
import gift.dto.request.OrderRequestDto;
import gift.dto.response.OrderResponseDto;
import gift.filter.AuthFilter;
import gift.filter.LoginFilter;
import gift.filter.MyTokenFilter;
import gift.filter.OAuthTokenFilter;
import gift.repository.token.TokenRepository;
import gift.service.OrderService;
import gift.service.TokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

import static gift.utils.FilterConstant.NO_AUTHORIZATION_REDIRECT_URL;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    MockMvc mvc;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private OrderService orderService;

    @MockBean
    TokenRepository tokenRepository;

    @Autowired
    ObjectMapper objectMapper;


    @Test
    @DisplayName("필터 통과 실패 테스트")
    void 필터_통과_실패_테스트() throws Exception {
        MockMvc mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilter(new AuthFilter(tokenRepository))
                .addFilter(new MyTokenFilter(tokenRepository))
                .addFilter(new OAuthTokenFilter(tokenRepository))
                .addFilter(new LoginFilter(tokenRepository))
                .build();

        mockMvc.perform(post("/api/orders"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("401"))
                .andExpect(jsonPath("$.message").value("인증되지 않은 사용자 입니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("주문 저장 시 OrderRequestDto Invalid 테스트")
    void 옵션_저장_DTO_INVALID_테스트() throws Exception{
        //given
        OrderRequestDto invalidOrderRequestDto = new OrderRequestDto(null, -1, null, -1);

        //expected
        mvc.perform(post("/api/orders")
                        .content(objectMapper.writeValueAsString(invalidOrderRequestDto))
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.optionId").value("옵션을 선택하세요."))
                .andExpect(jsonPath("$.validation.quantity").value("옵션 개수를 선택하세요. (1 이상)"))
                .andDo(print());
    }

    @Test
    @DisplayName("주문 성공 테스트")
    void 주문_성공_테스트() throws Exception{
        //given
        AuthToken authToken = new AuthToken("테스트용 인증코드", "123@kakao.com");
        OrderRequestDto orderRequestDto = new OrderRequestDto(1L, 100, "해피", 0);
        OrderResponseDto orderResponseDto = new OrderResponseDto(1L, 1L, 100, LocalDateTime.now(), "해피");

        given(tokenService.findToken("테스트용 인증코드")).willReturn(authToken);
        given(orderService.addOrder(orderRequestDto, authToken)).willReturn(orderResponseDto);

        //when
        mvc.perform(post("/api/orders")
                        .header("Authorization","Bearer 테스트용 인증코드")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequestDto))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.optionId").value("1"))
                .andExpect(jsonPath("$.quantity").value("100"))
                .andExpect(jsonPath("$.message").value("해피"))
                .andDo(print());
    }


}