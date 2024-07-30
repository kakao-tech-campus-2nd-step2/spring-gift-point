package gift.repository;

import gift.config.JpaConfig;
import gift.model.Orders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaConfig.class)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class OrderRepositoryTest {
    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("memberId로 주문 목록 조회 테스트[성공]")
    void findByMemberId() {
        // given
        Long memberId = 1L;
        Orders order1 = new Orders(1L, 1L, memberId,
                "pName1", "pUrl", "oName",
                1000, 10, "message");
        Orders order2 = new Orders(2L, 2L, memberId,
                "pName1", "pUrl", "oName",
                1000, 10, "message");
        orderRepository.save(order1);
        orderRepository.save(order2);
        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<Orders> actuals = orderRepository.findByMemberId(memberId, pageable);

        // then
        assertThat(actuals.getTotalElements()).isEqualTo(2);
        assertThat(actuals.getTotalPages()).isEqualTo(1);
        assertThat(actuals.getNumber()).isEqualTo(0);
        for (int i = 0; i < actuals.getContent().size(); i++) {
            assertThat(actuals.getContent().get(i).getProductId()).isEqualTo((long) i + 1);
            assertThat(actuals.getContent().get(i).getOptionId()).isEqualTo((long) i + 1);
            assertThat(actuals.getContent().get(i).getMemberId()).isEqualTo(memberId);
        }
    }
}