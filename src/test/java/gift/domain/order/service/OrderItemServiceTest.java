package gift.domain.order.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

import gift.domain.member.entity.AuthProvider;
import gift.domain.member.entity.Member;
import gift.domain.member.entity.Role;
import gift.domain.order.dto.OrderItemRequest;
import gift.domain.order.entity.Order;
import gift.domain.product.entity.Category;
import gift.domain.product.entity.Option;
import gift.domain.product.entity.Product;
import gift.domain.product.repository.ProductJpaRepository;
import gift.domain.product.service.OptionService;
import gift.domain.wishlist.repository.WishlistJpaRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@AutoConfigureMockMvc
@SpringBootTest
class OrderItemServiceTest {

    @Autowired
    private OrderItemService orderItemService;

    @MockBean
    private ProductJpaRepository productJpaRepository;

    @MockBean
    private WishlistJpaRepository wishlistJpaRepository;

    @MockBean
    private OptionService optionService;

    private static final Member MEMBER = new Member(1L, "testUser", "test@test.com", "test123", Role.USER, AuthProvider.LOCAL);
    private static final Category category = new Category(1L, "교환권", "#FFFFFF", "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png", "test");
    private static final Product product = new Product(1L, category, "아이스 카페 아메리카노 T", 4500, "https://image.istarbucks.co.kr/upload/store/skuimg/2021/04/[110563]_20210426095937947.jpg");
    private static final Option option1 = new Option(1L, product, "사과맛", 90);
    private static final Option option2 = new Option(2L, product, "포도맛", 70);


    @Test
    @DisplayName("구매 아이템 생성 테스트")
    void create_multiple_item() {
        // given
        product.addOption(option1);
        product.addOption(option2);

        Order order = new Order(1L, MEMBER, "testMessage");
        List<OrderItemRequest> orderItemRequests = List.of(
            new OrderItemRequest(1L, 70),
            new OrderItemRequest(2L, 30)
        );

        given(productJpaRepository.findByOptionId(anyLong())).willReturn(Optional.of(product));
        doNothing().when(optionService).subtractQuantity(anyLong(), anyInt());
        doNothing().when(wishlistJpaRepository).deleteByMemberAndProduct(any(Member.class), any(Product.class));

        // when
        orderItemService.createMultiple(MEMBER, order, orderItemRequests);

        // then
        assertThat(order.getOrderItems().size()).isEqualTo(2);
    }
}