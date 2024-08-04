package gift.EntityTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import gift.domain.MemberDomain.MemberPoint;

class MemberPointTest {

    @Test
    @DisplayName("포인트 설정 테스트")
    void testMemberPointInitialization() {
        MemberPoint memberPoint = new MemberPoint(1500);
        assertEquals(1500, memberPoint.getPoint());
    }

    @Test
    @DisplayName("getPoint테스트")
    void testDefaultConstructor() {
        MemberPoint memberPoint = new MemberPoint();
        assertEquals(0, memberPoint.getPoint());
    }

    @Test
    @DisplayName("포인트 사용 테스트")
    void testSubtractPoint() throws IllegalAccessException {
        MemberPoint memberPoint = new MemberPoint(1500);
        memberPoint.subtract(500);
        assertEquals(1000, memberPoint.getPoint());
    }

    @Test
    @DisplayName("1000이하일 때 포인트 사용 테스트")
    void testSubtractPointWithExceptionLessThan1000() {
        MemberPoint memberPoint = new MemberPoint(500);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            memberPoint.subtract(100);
        });
        assertEquals("포인트는 1000부터 사용 가능합니다.", exception.getMessage());
    }

    @Test
    @DisplayName("현재 포인트보다 사용량이 많을 때 테스트")
    void testSubtractPointWithExceptionMoreThanCurrentPoints() {
        MemberPoint memberPoint = new MemberPoint(1500);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            memberPoint.subtract(2000);
        });
        assertEquals("포인트보다 사용량이 더 많습니다.", exception.getMessage());
    }
}

