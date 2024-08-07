package gift.service;

import gift.domain.OptionDTO;
import gift.domain.OrderDTO;
import gift.entity.*;
import gift.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OptionRepository optionRepository;
    @Mock
    private OptionsRepository optionsRepository;
    @Mock
    private KakaoLoginService kakaoLoginService;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private WishlistRepository wishlistRepository;
    @Mock
    private OptionService optionService;
    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddOrder() {
        // Given
        String token = "testToken";
        int memberId = 1;
        int productId = 1;
        OptionDTO optionDTO = new OptionDTO("test", 1);
        Option option = new Option(optionDTO);

        Category testCategory = new Category(1, "test", "test", "test", "test");
        Product product = new Product(1, testCategory, 1, "test", "test");
        Member member = new Member(1, "test", "test", "test");

        OrderDTO orderDTO = new OrderDTO(1, 2, "Test message");
        Order order = new Order(option, 2, "timestamp", "Test message");

        option.setId(1);


        when(optionRepository.findById(1)).thenReturn(Optional.of(option));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(optionService.deductQuantity(1, 2)).thenReturn(1);
        when(memberRepository.searchIdByToken(token)).thenReturn(memberId);
        when(optionsRepository.findProductIdByOptionListContaining(option)).thenReturn(Optional.of(productId));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // When
        Order result = orderService.addOrder(token, orderDTO);

        // Then
        assertNotNull(result);
        assertEquals(option, result.getOption());
        assertEquals(1, result.getOption().getQuantity());
        assertEquals("Test message", result.getMessage());
    }

    @Test
    void testAddOrderOptionNotFound() {
        // Given
        String token = "testToken";
        OrderDTO orderDTO = new OrderDTO(1, 2, "Test message");

        when(optionRepository.findById(1)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoSuchElementException.class, () -> orderService.addOrder(token, orderDTO));
        verify(optionRepository).findById(1);
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void testFindOrderById() {
        // Given
        int orderId = 0;
        OptionDTO optionDTO = new OptionDTO("test", 1);
        Option option = new Option(optionDTO);

        Order order = new Order(option, 2, "timestamp", "Test message");

        option.setId(orderId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // When
        Order result = orderService.findOrderById(orderId);

        // Then
        assertNotNull(result);
        assertEquals(orderId, result.getId());
        verify(orderRepository).findById(orderId);
    }

    @Test
    void testFindOrderByIdNotFound() {
        // Given
        int orderId = 1;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoSuchElementException.class, () -> orderService.findOrderById(orderId));
        verify(orderRepository).findById(orderId);
    }
}