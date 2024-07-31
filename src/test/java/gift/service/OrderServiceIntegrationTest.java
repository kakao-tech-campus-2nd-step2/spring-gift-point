package gift.service;

import gift.entity.option.Option;
import gift.entity.option.OptionDTO;
import gift.entity.order.OrderDTO;
import gift.entity.product.Product;
import gift.entity.product.ProductDTO;
import gift.entity.user.UserDTO;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
public class OrderServiceIntegrationTest {

    private HttpSession session;

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


    @Test
    void orderSaveTest() {
        // given
        // setup
        String testEmail = "test@gmail.com";
        session = mock(HttpSession.class);
        when(session.getAttribute("email")).thenReturn(testEmail);

        // 회원가입
        userService.signup(new UserDTO(testEmail, "test"));

        // 상품
        Product product = productService.save(new ProductDTO("test1", 123, "test.com", 1L), testEmail);

        // 상품에 옵션 추가
        List<Option> options = productService.addProductOption(product.getId(), List.of(new OptionDTO("abc", 100)), testEmail);

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


}
