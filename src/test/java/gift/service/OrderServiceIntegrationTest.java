package gift.service;

import gift.entity.option.Option;
import gift.entity.option.OptionDTO;
import gift.entity.order.OrderDTO;
import gift.entity.product.Product;
import gift.entity.product.ProductDTO;
import gift.entity.user.User;
import gift.entity.user.UserDTO;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
public class OrderServiceIntegrationTest {

    private HttpSession session;
    private String testEmail;
    private Product product;
    private List<Option> options;

    @Autowired
    private WishlistService wishlistService;
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OptionService optionService;
    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
        testEmail = "test@gmail.com";
        userService.signup(new UserDTO(testEmail, "test"));

        session = mock(HttpSession.class);
        when(session.getAttribute("email")).thenReturn(testEmail);

        // 상품
        product = productService.save(new ProductDTO("test1", 123, "test.com", -1L), testEmail);

        // 상품에 옵션 추가
        options = productService.addProductOption(product.getId(), List.of(new OptionDTO("abc", 100)), testEmail);
    }

    @Test
    void orderSaveTest() throws InterruptedException {
        // given
        // 위시리스트에 추가
        wishlistService.addWishlistProduct(testEmail, product.getId());

        // when
        orderService.save(session, new OrderDTO(product.getId(), options.get(0).getId(), 30, "test msg"));

        // then
        // 옵션 quantity 차감 확인
        Option expectOption = optionService.findById(options.get(0).getId());
        assertThat(expectOption.getQuantity()).isEqualTo(70);

        // wishlist에서 삭제 확인
        List<Product> expectProducts = wishlistService.getWishlistProducts(testEmail);
        assertThat(expectProducts.size()).isEqualTo(0);
    }

    @Test
    void 가지고_있는_포인트보다_더_많이_쓴_경우() {
        // given
        User user = userService.findOne(testEmail);

        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> orderService.save(session, new OrderDTO(product.getId(), options.get(0).getId(), 30, "test msg", 100)));

        // then
        assertThat(exception.getMessage()).isEqualTo("Illegal order");
    }

    @Test
    void 가격보다_포인트를_더_많이_쓴_경우() {
        // given
        int quantity = 30;
        int overPrice = product.getPrice() * quantity + 1;

        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> orderService.save(session, new OrderDTO(product.getId(), options.get(0).getId(), quantity, "test msg", overPrice)));

        // then
        assertThat(exception.getMessage()).isEqualTo("Illegal order");
    }

    @Test
    void 정상적으로_포인트가_사용된_경우() throws InterruptedException {
        // given
        User user = userService.findOne(testEmail);
        int quantity = 30;
        int totalPrice = product.getPrice() * quantity;
        user.setPoint(totalPrice);

        // when
        orderService.save(session, new OrderDTO(product.getId(), options.get(0).getId(), quantity, "test msg", totalPrice));

        // then
        User expect = userService.findOne(testEmail);
        assertThat(expect.getPoint()).isEqualTo(0);
    }
}
