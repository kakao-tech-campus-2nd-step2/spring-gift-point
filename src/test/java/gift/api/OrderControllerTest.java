package gift.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.global.security.JwtFilter;
import gift.global.security.JwtUtil;
import gift.member.validator.LoginMember;
import gift.member.validator.LoginMemberArgumentResolver;
import gift.order.api.OrderController;
import gift.order.application.OrderService;
import gift.order.dto.OrderRequest;
import gift.order.dto.OrderResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @InjectMocks
    private JwtFilter jwtFilter;
    @MockBean
    private JwtUtil jwtUtil;
    @MockBean
    private LoginMemberArgumentResolver loginMemberArgumentResolver;
    @MockBean
    private OrderService orderService;
    private final String bearerToken = "Bearer token";

    @Test
    @DisplayName("상품 주문하기 기능 테스트")
    void orderProduct() throws Exception {
        Long memberId = 1L;
        OrderRequest request = new OrderRequest(
                2L,
                1,
                "message"
        );
        OrderResponse response = new OrderResponse(
                1L,
                request.optionId(),
                request.quantity(),
                LocalDateTime.now(),
                "message"
        );

        String requestJson = objectMapper.writeValueAsString(request);
        String responseJson = objectMapper.writeValueAsString(response);

        given(orderService.order(anyLong(), any()))
                .willReturn(response);
        given(loginMemberArgumentResolver.supportsParameter(argThat(parameter ->
                parameter.hasParameterAnnotation(LoginMember.class)))).willReturn(true);
        given(loginMemberArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .willReturn(memberId);

        mockMvc.perform(post("/api/orders")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(content().json(responseJson));

        verify(orderService).order(memberId, request);
    }

}