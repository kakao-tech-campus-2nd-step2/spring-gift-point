package gift.service;

import static org.junit.jupiter.api.Assertions.assertAll;

import gift.repository.JpaGiftOptionRepository;
import gift.order.dto.OrderRequest;
import gift.order.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private JpaGiftOptionRepository jpaGiftOptionRepository;

    @BeforeEach
    void setUp() {

    }



    @Test
    @DisplayName("주문 정상 동작 확인")
    void sucessOrder() {

        // given


        Long memberId = 1L;
        Long productId = 1L;
        Long giftOptionId = 1L;
        Integer quantity = 1;
        String msg = "주문 만드러용";

        OrderRequest orderRequest = new OrderRequest(memberId, productId, giftOptionId,quantity, msg);
        // when

        orderService.create(orderRequest);

        // then

        assertAll(
            // 상품 옵션 수량이 부족할 경우 실패

            // 해당 상품이 wishList에 있는 경우 삭제됨.

        );



    }
}
