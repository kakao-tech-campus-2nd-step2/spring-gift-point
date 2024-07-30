package gift.controller;

import gift.dto.OrderDTO;
import gift.service.OrderService;
import gift.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    private String token;
    private OrderDTO orderDTO;

    @BeforeEach
    public void setUp() {
        token = "your-jwt-token";
        orderDTO = new OrderDTO();
        orderDTO.setOptionId(1L);
        orderDTO.setQuantity(2);
        orderDTO.setMessage("Please handle this order with care.");
    }

    @Test
    public void testCreateOrder() throws Exception {
        Mockito.when(jwtTokenProvider.getUsernameFromToken(token)).thenReturn("user@example.com");
        Mockito.when(orderService.createOrder(Mockito.any(OrderDTO.class), Mockito.anyString())).thenReturn(orderDTO);

        mockMvc.perform(post("/api/orders")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"optionId\":1,\"quantity\":2,\"message\":\"Please handle this order with care.\"}"))
                .andExpect(status().isCreated());
    }
}