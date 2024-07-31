package gift.service;

import gift.entity.*;
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
        session = mock(HttpSession.class);
        when(session.getAttribute("email")).thenReturn("test@gmail.com");

        // 회원가입
        String testEmail = "test@gmail.com";
        userService.signup(new UserDTO(testEmail, "test"));

        // 상품
        Product product = productService.save(new ProductDTO("test1", 123, "test.com", 1L), testEmail);

        // 옵션
        Option option = optionService.save(new OptionDTO("abc", 100), testEmail);

        // 상품에 옵션 추가
        productService.addProductOption(product.getId(), option.getId(), testEmail);

        // 위시리스트에 추가
        wishlistService.addWishlistProduct(testEmail, new WishlistDTO(product.getId()));

        // when
        orderService.save(session, new OrderDTO(product.getId(), option.getId(), 30, "test msg"));

        // then
        // 옵션 quantity 차감 확인
        Option expectOption = optionService.findById(option.getId());
        assertThat(expectOption.getQuantity()).isEqualTo(70);

        // wishlist에서 삭제 확인
        List<Product> expectProducts = wishlistService.getWishlistProducts(testEmail);
        assertThat(expectProducts.size()).isEqualTo(0);
    }


}
