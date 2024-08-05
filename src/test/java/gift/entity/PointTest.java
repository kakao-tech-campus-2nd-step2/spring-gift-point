package gift.entity;

import gift.exception.InsufficientPointException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class PointTest {

    private Point point;

    @BeforeEach
    void setUp() {
        point = new Point();
    }


    @Test
    @DisplayName("초기 포인트가 0인가")
    void initPointZero() {
        assertThat(point.getBalance()).isEqualTo(0);
    }

    @Test
    @DisplayName("포인트 빼기 - 성공")
    void subtractBalanceSuccess() {
        point.updateBalance(1000);
        point.subtractBalance(200);
        assertThat(point.getBalance()).isEqualTo(810);
    }

    @Test
    @DisplayName("포인트 빼기 - 실패")
    void subtractBalanceFail() {
        point.updateBalance(949);
        assertThatThrownBy(() -> point.subtractBalance(1000))
                .isInstanceOf(InsufficientPointException.class);
    }

    @Test
    @DisplayName("포인트 수정 - 성공")
    void testUpdateBalanceSuccess() {
        point.updateBalance(119);
        assertThat(point.getBalance()).isEqualTo(119);
    }

    @Test
    @DisplayName("포인트 수정 - 실패")
    void testUpdateBalanceFail() {
        point.updateBalance(119);
        assertThatThrownBy(() -> point.updateBalance(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
