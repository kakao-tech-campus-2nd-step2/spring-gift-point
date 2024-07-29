package gift.service;

import gift.client.KakaoApiClient;
import gift.dto.OrderRequestDto;
import gift.dto.OrderResponseDto;
import gift.entity.*;
import gift.exception.BusinessException;
import gift.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductOptionRepository productOptionRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WishRepository wishRepository;

    @MockBean
    private KakaoApiClient kakaoApiClient;

    @MockBean
    private TokenService tokenService;

    @AfterEach
    public void tearDown() {
        wishRepository.deleteAll();
        orderRepository.deleteAll();
        productOptionRepository.deleteAll();
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        optionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void 주문_생성_성공() {
        User testUser = userRepository.save(new User("test@example.com", "password123"));
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("id", testUser.getId().toString());
        when(tokenService.extractUserInfo(anyString())).thenReturn(userInfo);

        Category category = categoryRepository.save(new Category("Test Category", "#FF0000", "https://example.com/test.png", "Test Description"));
        Product product = productRepository.save(new Product(new ProductName("Test Product"), 1000, "https://example.com/image.jpg", category));
        Option option = optionRepository.save(new Option(new OptionName("Test Option")));
        ProductOption productOption = productOptionRepository.save(new ProductOption(product, option, 10));
        OrderRequestDto orderRequestDto = new OrderRequestDto(productOption.getId(), 2, "Test Message");

        OrderService orderService = new OrderService(orderRepository, productOptionRepository, userRepository, wishRepository, kakaoApiClient, tokenService);
        OrderResponseDto orderResponseDto = orderService.createOrder("Bearer jwtToken", "Bearer kakaoAccessToken", orderRequestDto);

        assertNotNull(orderResponseDto);
        assertEquals(productOption.getId(), orderResponseDto.getProductOptionId());
        assertEquals(2, orderResponseDto.getQuantity());
        assertEquals("Test Message", orderResponseDto.getMessage());
    }

    @Test
    public void 주문_생성_실패_수량부족() {
        User testUser = userRepository.save(new User("test@example.com", "password123"));
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("id", testUser.getId().toString());
        when(tokenService.extractUserInfo(anyString())).thenReturn(userInfo);

        Category category = categoryRepository.save(new Category("Test Category", "#FF0000", "https://example.com/test.png", "Test Description"));
        Product product = productRepository.save(new Product(new ProductName("Test Product"), 1000, "https://example.com/image.jpg", category));
        Option option = optionRepository.save(new Option(new OptionName("Test Option")));
        ProductOption productOption = productOptionRepository.save(new ProductOption(product, option, 1));
        OrderRequestDto orderRequestDto = new OrderRequestDto(productOption.getId(), 2, "Test Message");

        OrderService orderService = new OrderService(orderRepository, productOptionRepository, userRepository, wishRepository, kakaoApiClient, tokenService);

        assertThrows(BusinessException.class, () -> orderService.createOrder("Bearer jwtToken", "Bearer kakaoAccessToken", orderRequestDto));
    }

    @Test
    public void 주문_생성_후_카카오_메시지_전송_성공() {
        User testUser = userRepository.save(new User("test@example.com", "password123"));
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("id", testUser.getId().toString());
        when(tokenService.extractUserInfo(anyString())).thenReturn(userInfo);

        Category category = categoryRepository.save(new Category("Test Category", "#FF0000", "https://example.com/test.png", "Test Description"));
        Product product = productRepository.save(new Product(new ProductName("Test Product"), 1000, "https://example.com/image.jpg", category));
        Option option = optionRepository.save(new Option(new OptionName("Test Option")));
        ProductOption productOption = productOptionRepository.save(new ProductOption(product, option, 10));
        OrderRequestDto orderRequestDto = new OrderRequestDto(productOption.getId(), 2, "Test Message");

        OrderService orderService = new OrderService(orderRepository, productOptionRepository, userRepository, wishRepository, kakaoApiClient, tokenService);
        orderService.createOrder("Bearer jwtToken", "Bearer kakaoAccessToken", orderRequestDto);

        verify(kakaoApiClient, times(1)).sendMessageToMe(anyString(), any(Order.class));
    }
}
