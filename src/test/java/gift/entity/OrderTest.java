package gift.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    @DisplayName("생성자 테스트")
    void OrderConstructorTest() {
        Order newOrder = new Order(new Option(), new User(), 50, LocalDateTime.now(),
            "newTestMessage");

        assertThat(newOrder).isNotNull();
        assertThat(newOrder.getQuantity()).isEqualTo(50);
        assertThat(newOrder.getMessage()).isEqualTo("newTestMessage");
    }

}
