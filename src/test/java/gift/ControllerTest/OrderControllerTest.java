package gift.ControllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.controller.OrderController;
import gift.domain.Order.Order;
import gift.domain.Order.OrderRequest;
import gift.service.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("주문하기")
    void orderTest() throws Exception {
        OrderRequest orderRequest = new OrderRequest(1L,2L,"문 앞에 놔주세요");

        Order order = new Order(1L, 2L, 3L, new Date(), "message");
        given(orderService.order(anyString(), any(OrderRequest.class))).willReturn(order);

        mockMvc.perform(post("/api/orders")
                        .header("Authorization", "Bearer 5NdKZ6Omw6wox5MWgCDasRvdHe6lVOaLAAAAAQo9cxgAAAGQ-mPU7rG7d-HwzTGR")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(order)));
    }
}
