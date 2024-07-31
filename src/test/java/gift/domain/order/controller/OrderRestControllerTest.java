package gift.domain.order.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.auth.jwt.JwtProvider;
import gift.domain.member.entity.Member;
import gift.domain.order.dto.OrderItemRequest;
import gift.domain.order.dto.OrderRequest;
import gift.domain.order.dto.OrderResponse;
import gift.domain.order.service.OrderService;
import gift.domain.product.entity.Category;
import gift.domain.product.entity.Option;
import gift.domain.product.entity.Product;
import gift.domain.member.entity.AuthProvider;
import gift.domain.member.entity.Role;
import gift.domain.member.repository.MemberJpaRepository;
import io.jsonwebtoken.Claims;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
class OrderRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private MemberJpaRepository memberJpaRepository;

    @MockBean
    private JwtProvider jwtProvider;

    @Autowired
    private ObjectMapper objectMapper;

    private static final Member MEMBER = new Member(1L, "testUser", "test@test.com", "test123", Role.USER, AuthProvider.LOCAL);
    private static final Category category = new Category(1L, "교환권", "#FFFFFF", "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png", "test");
    private static final Product product = new Product(1L, category, "아이스 카페 아메리카노 T", 4500, "https://image.istarbucks.co.kr/upload/store/skuimg/2021/04/[110563]_20210426095937947.jpg");
    private static final Option option1 = new Option(1L, product, "사과맛", 90);
    private static final Option option2 = new Option(2L, product, "포도맛", 70);


    @BeforeEach
    void setUp() {
        given(memberJpaRepository.findById(any(Long.class))).willReturn(Optional.of(MEMBER));

        Claims claims = Mockito.mock(Claims.class);
        given(jwtProvider.getAuthentication(any(String.class))).willReturn(claims);
        given(claims.getSubject()).willReturn(String.valueOf(MEMBER.getId()));
    }

    @Test
    @DisplayName("주문 생성 테스트")
    void create() throws Exception {
        // given
        product.addOption(option1);
        product.addOption(option2);

        List<OrderItemRequest> orderItemRequests = List.of(
            new OrderItemRequest(1L, 1L, 10),
            new OrderItemRequest(1L, 2L, 30)
        );
        OrderRequest orderRequest = new OrderRequest(orderItemRequests, "Test Message", 180000);
        OrderResponse orderResponse = OrderResponse.from(orderRequest.toOrder(MEMBER));

        given(orderService.createAndSendMessage(any(OrderRequest.class), any(Member.class))).willReturn(orderResponse);

        // when & then
        mockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(orderRequest))
            .header("Authorization", "Bearer token"))
            .andExpect(status().isCreated())
            .andExpect(content().json(objectMapper.writeValueAsString(orderResponse)));
    }
}