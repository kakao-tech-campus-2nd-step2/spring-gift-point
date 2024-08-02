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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
                "message",
                100
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

    @Test
    @DisplayName("상품 전체 조회 기능 테스트")
    void getOrders() throws Exception {
        List<OrderResponse> orders = new ArrayList<>();
        OrderResponse orderResponse1 = new OrderResponse(1L, 2L, 3, LocalDateTime.now(), "message1");
        OrderResponse orderResponse2 = new OrderResponse(2L, 3L, 1, LocalDateTime.now(), "message2");
        orders.add(orderResponse1);
        orders.add(orderResponse2);

        Page<OrderResponse> response = new PageImpl<>(orders);
        String responseJson = objectMapper.writeValueAsString(response);
        given(orderService.getPagedOrders(any()))
                .willReturn(response);

        mockMvc.perform(get("/api/orders")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken))
                .andExpect(status().isOk())
                .andExpect(content().json(responseJson));

        verify(orderService).getPagedOrders(any());
    }

}