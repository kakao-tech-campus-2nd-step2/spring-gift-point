package gift.order.controller;

import gift.auth.TokenService;
import gift.member.application.MemberService;
import gift.member.application.response.MemberServiceResponse;
import gift.member.presentation.request.ResolvedMember;
import gift.order.application.OrderService;
import gift.order.application.command.OrderCreateCommand;
import gift.order.application.response.OrderSaveServiceResponse;
import gift.order.controller.request.OrderCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private MemberService memberService;

    @MockBean
    private TokenService tokenService;

    private OrderCreateRequest orderCreateRequest;
    private OrderSaveServiceResponse orderSaveServiceResponse;

    String token;
    Long memberId;
    String email;
    String password;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        memberId = 1L;
        email = "test@example.com";
        password = "password";
        orderCreateRequest = new OrderCreateRequest(1L, 6, "Order Message");
        orderSaveServiceResponse = new OrderSaveServiceResponse(1L, LocalDateTime.now(), 60000, 59000);
        token = "testToken";
    }

    @Test
    void 유효한_주문_요청_성공() throws Exception {
        // Given
        MemberServiceResponse memberServiceResponse = new MemberServiceResponse(memberId, email, password);
        when(memberService.findById(anyLong())).thenReturn(memberServiceResponse);
        when(orderService.save(any(OrderCreateCommand.class), any(Long.class))).thenReturn(orderSaveServiceResponse);

        // When & Then
        mockMvc.perform(post("/api/orders")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"optionId\": 1, \"quantity\": 6, \"message\": \"Order Message\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(orderSaveServiceResponse.id()))
                .andExpect(jsonPath("$.optionId").value(orderCreateRequest.optionId()))
                .andExpect(jsonPath("$.quantity").value(orderCreateRequest.quantity()))
                .andExpect(jsonPath("$.message").value(orderCreateRequest.message()))
                .andExpect(jsonPath("$.orderDateTime").isNotEmpty())
                .andExpect(jsonPath("$.originalPrice").value(orderSaveServiceResponse.originalPrice()))
                .andExpect(jsonPath("$.finalPrice").value(orderSaveServiceResponse.finalPrice()));
    }
}
