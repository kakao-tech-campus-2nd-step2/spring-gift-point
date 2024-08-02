package gift.service;

import gift.dto.point.PointRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@Transactional
class PointServiceTest {

    @Autowired
    private PointService pointService;

    @Test
    @DisplayName("포인트를 추가하면 늘어난다")
    void successAddPoint() {
        //given
        var pointRequest = new PointRequest(1000);
        var memberPoint = pointService.getPoint(1L);
        //when
        var savedPoint = pointService.addPoint(1L, pointRequest.point());
        //then
        Assertions.assertThat(savedPoint.point() - memberPoint.point()).isEqualTo(1000);
    }
}
