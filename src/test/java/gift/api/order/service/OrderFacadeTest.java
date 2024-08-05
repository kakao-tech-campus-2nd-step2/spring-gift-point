package gift.api.order.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.willDoNothing;

import gift.api.member.service.KakaoService;
import gift.api.order.dto.OrderRequest;
import gift.api.order.dto.OrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class OrderFacadeTest {

    @Autowired
    private OrderFacade orderFacade;
    @MockBean
    private KakaoService kakaoService;

    @BeforeEach
    public void setup() {
        willDoNothing().given(kakaoService).sendMessage(any(), any());
    }

    @Test
    void order() {
        // given
        var memberId = 1L;
        var optionId = 1L;
        var quantity = 7;
        var message = "My gift for you.";
        var point = 0;
        var orderRequest = new OrderRequest(optionId, quantity, message, point);

        // when
        OrderResponse actual = orderFacade.order(memberId, orderRequest);

        // then
        assertAll(
            () -> assertThat(actual.optionId()).isEqualTo(optionId),
            () -> assertThat(actual.quantity()).isEqualTo(quantity),
            () -> assertThat(actual.message()).isEqualTo(message),
            () -> assertThat(actual.point()).isEqualTo(point)
        );
    }
}