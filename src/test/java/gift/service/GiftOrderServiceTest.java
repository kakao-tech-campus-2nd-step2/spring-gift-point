package gift.service;

import gift.dto.giftorder.GiftOrderRequest;
import gift.exception.NotFoundElementException;
import gift.repository.OptionRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@Transactional
class GiftOrderServiceTest {

    @Autowired
    private GiftOrderService giftOrderService;
    @Autowired
    private PointService pointService;
    @Autowired
    private OptionRepository optionRepository;

    @Test
    @DisplayName("구매를 진행하면 포인트가 차감된다")
    void successGiftOrder() {
        //given
        var memberPoint = pointService.addPoint(1L, 100000);
        var orderRequest = new GiftOrderRequest(2L, 100, "message", 50000);
        var option = optionRepository.findById(2L)
                .orElseThrow(() -> new NotFoundElementException(2L + "를 가진 옵션이 존재하지 않습니다."));
        //when
        var order = giftOrderService.addGiftOrder(1L, option, orderRequest);
        //then
        var subtractedPoint = pointService.getPoint(1L);
        Assertions.assertThat(memberPoint.point() - subtractedPoint.point()).isEqualTo(50000);

        giftOrderService.deleteOrder(order.id());
    }
}
