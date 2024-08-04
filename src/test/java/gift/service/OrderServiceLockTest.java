package gift.service;

import gift.dto.option.OptionRequestDTO;
import gift.dto.order.OrderDTO;
import gift.dto.product.ProductRequestDto;
import gift.dto.product.ProductResponseDto;
import gift.entity.Option;
import gift.entity.User;
import gift.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class OrderServiceLockTest {

    private HttpSession session;
    private String testEmail;
    private ProductResponseDto product;
    private List<Option> options;

    @Autowired
    private ProductService productService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        testEmail = "test@gmail.com";
        User user = new User(testEmail, "test");
        user.setPoint(100);
        userRepository.save(user);

        session = mock(HttpSession.class);
        when(session.getAttribute("email")).thenReturn(testEmail);

        // 상품
        product = productService.save(new ProductRequestDto("test1", 123, "test.com", -1L), testEmail);

        // 상품에 옵션 추가
        options = productService.addProductOption(product.getId(), List.of(new OptionRequestDTO("abc", 10000)), testEmail);

    }

    @Test
    void 한_사람이_동시에_주문_넣을_때의_포인트_동시성_테스트() throws InterruptedException {


        final int threadCount = 10;
        final ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        final CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    orderService.save(session, new OrderDTO(product.getId(), options.get(0).getId(), 1, "test msg", 10));
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        User updatedUser = userRepository.findByEmail(testEmail).orElseThrow();

        // then
        assertThat(updatedUser.getPoint()).isEqualTo(0);
    }
}
