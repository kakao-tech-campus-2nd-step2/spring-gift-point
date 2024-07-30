package gift;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.api.OrderResponse;
import gift.dto.OrderDTO;
import gift.dto.PageRequestDTO;
import gift.service.OrderService;
import java.time.LocalDateTime;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        // 필요시 초기화 코드 추가
    }

    @Test
    @Transactional
    public void createOrder_Success() throws Exception {
        OrderDTO orderDTO = new OrderDTO(null, 1L, 2, "Order message", LocalDateTime.now());
        OrderResponse orderResponse = new OrderResponse(1L, 1L, 2, LocalDateTime.now(), "Order message");

        Mockito.when(orderService.createOrder(Mockito.anyString(), Mockito.any(OrderDTO.class))).thenReturn(orderResponse);

        String accessToken = "some-valid-token";

        mockMvc.perform(post("/api/orders")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderDTO)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(orderResponse.getId()))
            .andExpect(jsonPath("$.optionId").value(orderResponse.getOptionId()))
            .andExpect(jsonPath("$.quantity").value(orderResponse.getQuantity()))
            .andExpect(jsonPath("$.message").value(orderResponse.getMessage()));
    }
    

}
