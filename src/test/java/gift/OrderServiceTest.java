package gift;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.dto.OrderRequestDto;
import gift.dto.OrderResponseDto;
import gift.entity.Category;
import gift.entity.Member;
import gift.entity.Option;
import gift.entity.Order;
import gift.entity.Product;
import gift.exception.LoginException;
import gift.exception.OptionException;
import gift.exception.WishException;
import gift.repository.MemberRepository;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import gift.service.KakaoService;
import gift.service.OrderService;
import gift.service.WishService;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private OptionRepository optionRepository;

    @Mock
    private WishService wishService;

    @Mock
    private KakaoService kakaoService;

    @InjectMocks
    private OrderService orderService;


    @Test
    @DisplayName("주문 추가 테스트")
    void addOrderTest() throws JsonProcessingException, WishException {
        String email = "test@kakao.com";
        OrderRequestDto orderRequestDto = new OrderRequestDto(1L, 2, "Test");

        Member member = new Member(email, "password");
        Product product = new Product("치킨", 20000, "chicken.com", new Category("음식"));
        Option option = new Option("Option", 10, product);
        option.setId(1L);

        Order order = new Order(2, LocalDateTime.now(), "Test", option, member);
        order.setId(1L);
        when(memberRepository.findByEmail(email)).thenReturn(Optional.of(member));
        when(optionRepository.findById(1L)).thenReturn(Optional.of(option));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order savedOrder = invocation.getArgument(0);
            savedOrder.setId(1L);
            return savedOrder;
        });

        OrderResponseDto response = orderService.addOrder(email, orderRequestDto);
        assertEquals(1L, response.getId());
        assertEquals(orderRequestDto.getOptionId(), response.getOptionId());
    }

    @Test
    @DisplayName("올바르지 않은 사용자 테스트")
    void invalidUserTest() {
        String email = "not@kakao.com";
        OrderRequestDto orderRequestDto = new OrderRequestDto(1L, 2, "Test");

        when(memberRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(LoginException.class, () -> {
            orderService.addOrder(email, orderRequestDto);
        });
    }

    @Test
    @DisplayName("올바르지 않은 옵션 테스트")
    void invalidOptionTest() {
        String email = "option@kakao.com";
        OrderRequestDto orderRequestDto = new OrderRequestDto(1L, 2, "Test");

        Member member = new Member(email, "password");

        when(memberRepository.findByEmail(email)).thenReturn(Optional.of(member));
        when(optionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(OptionException.class, () -> {
            orderService.addOrder(email, orderRequestDto);
        });
    }

    @Test
    @DisplayName("수량 테스트")
    void quantityTest() {
        String email = "quantity@kakao.com";
        OrderRequestDto orderRequestDto = new OrderRequestDto(1L, 20, "Test message");

        Member member = new Member(email, "password");
        Option option = new Option("Option", 10, null);

        when(memberRepository.findByEmail(email)).thenReturn(Optional.of(member));
        when(optionRepository.findById(1L)).thenReturn(Optional.of(option));

        assertThrows(OptionException.class, () -> {
            orderService.addOrder(email, orderRequestDto);
        });
    }
}
