package gift.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.service.OrderService;
import gift.service.TokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = OrderController.class)
@ActiveProfiles("test")
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private TokenService tokenService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("주문 생성 테스트")
    void createOrder() throws Exception {
        OrderRequest orderRequest = new OrderRequest(1L, 2, "Happy Birthday!");
        OrderResponse orderResponse = new OrderResponse(1L, 1L, 2, LocalDateTime.now(), "Order created successfully.");

        Mockito.when(orderService.createOrder(any(OrderRequest.class), eq("valid-token")))
                .thenReturn(orderResponse);

        mockMvc.perform(post("/api/orders")
                        .header("Authorization", "Bearer valid-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(orderResponse.getId()))
                .andExpect(jsonPath("$.message").value(orderResponse.getMessage()));

        Mockito.verify(orderService).createOrder(any(OrderRequest.class), eq("valid-token"));
    }

    @Test
    @DisplayName("주문 생성 실패 테스트")
    void createOrderFailure() throws Exception {
        String authorizationHeader = "Bearer test-token";
        OrderRequest orderRequest = new OrderRequest(1L, 2, "Happy Birthday!");

        Mockito.when(orderService.createOrder(any(OrderRequest.class), eq("test-token")))
                .thenThrow(new RuntimeException("Order processing failed"));

        mockMvc.perform(post("/api/orders")
                        .header("Authorization", authorizationHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isInternalServerError());

        Mockito.verify(orderService).createOrder(any(OrderRequest.class), eq("test-token"));
    }
}