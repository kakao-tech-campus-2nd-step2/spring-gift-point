package gift.member.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PointTest {

    @Test
    @DisplayName("포인트 사용 테스트")
    void usePointTest() {
        //given
        var point = Point.of(100);

        //when & then
        Assertions.assertThatThrownBy(() -> point.use(101))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("포인트가 부족합니다.");
    }

    @Test
    @DisplayName("포인트 사용 테스트")
    void accumulatePointTest() {
        //given
        var point = Point.of(100);

        //when
        var accumulatedPoint = point.accumulate(1000);

        //then
        Assertions.assertThat(accumulatedPoint).isEqualTo(100);
        Assertions.assertThat(point).isEqualTo(Point.of(200));
    }
}