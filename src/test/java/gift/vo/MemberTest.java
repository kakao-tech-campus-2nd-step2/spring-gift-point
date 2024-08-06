package gift.vo;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {

    @Test
    @DisplayName("Test for Insufficient Points.")
    void insufficientPoints() {
        // given
        Member member = new Member("test@example.com", "password");
        member.updatePoint(100);

        // when & then
        assertThatThrownBy(() -> member.subtractPoint(1000))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("보유한 포인트가 부족합니다. 현재 보유 포인트: 100차감하려는 포인트: 1000. 포인트를 충전한 후 다시 시도해 주세요.");

    }

    @Test
    @DisplayName("Test for Sufficient Points.")
    void sufficientPoints() {
        // given
        Member member = new Member("test@example.com", "password");
        member.updatePoint(30);

        // when
        member.subtractPoint(10);

        // then
        assertThat(member.getPoint()).isEqualTo(20);
    }

}