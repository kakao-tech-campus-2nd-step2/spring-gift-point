package gift.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.service.OrderService;
import gift.service.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private OrderController orderController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
        this.objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("이메일 토큰으로 주문생성 테스트")
    void testCreateOrderWithJwtToken() throws Exception {
        OrderRequest orderRequest = new OrderRequest(1L, 2, "Test message");

        OrderResponse orderResponse = new OrderResponse(1L, 1L, 2, LocalDateTime.now(), "Test message");

        when(tokenService.isJwtToken(any(String.class))).thenReturn(true);
        when(orderService.createOrder(any(OrderRequest.class), any(String.class), eq(false))).thenReturn(orderResponse);

        mockMvc.perform(post("/api/orders")
                        .header("Authorization", "Bearer test.jwt.token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderResponse.getId()))
                .andExpect(jsonPath("$.optionId").value(orderResponse.getOptionId()))
                .andExpect(jsonPath("$.quantity").value(orderResponse.getQuantity()))
                .andExpect(jsonPath("$.message").value(orderResponse.getMessage()));
    }

    @Test
    @DisplayName("카카오 토큰으로 주문생성 테스트")
    void testCreateOrderWithKakaoToken() throws Exception {
        OrderRequest orderRequest = new OrderRequest(1L, 2, "Test message");

        OrderResponse orderResponse = new OrderResponse(1L, 1L, 2, LocalDateTime.now(), "Test message");

        when(tokenService.isJwtToken(any(String.class))).thenReturn(false);
        when(orderService.createOrder(any(OrderRequest.class), any(String.class), eq(true))).thenReturn(orderResponse);

        mockMvc.perform(post("/api/orders")
                        .header("Authorization", "Bearer test.kakao.token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderResponse.getId()))
                .andExpect(jsonPath("$.optionId").value(orderResponse.getOptionId()))
                .andExpect(jsonPath("$.quantity").value(orderResponse.getQuantity()))
                .andExpect(jsonPath("$.message").value(orderResponse.getMessage()));
    }
}