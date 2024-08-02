package gift.service;

import gift.dto.PointChargeRequestDto;
import gift.dto.PointResponseDto;
import gift.entity.Point;
import gift.entity.User;
import gift.exception.BusinessException;
import gift.exception.ErrorCode;
import gift.repository.PointRepository;
import gift.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PointServiceTest {

    @Autowired
    private PointService pointService;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    public void tearDown() {
        pointRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void 포인트_조회_성공() {
        User user = userRepository.save(new User("testuser@example.com", "password"));
        pointRepository.save(new Point(user));

        PointResponseDto pointResponseDto = pointService.getUserPoints(user.getId());

        assertNotNull(pointResponseDto);
        assertEquals(user.getId(), pointResponseDto.getUserId());
        assertEquals(0, pointResponseDto.getPoints());
    }

    @Test
    public void 포인트_충전_성공() {
        User user = userRepository.save(new User("testuser@example.com", "password"));
        Point point = pointRepository.save(new Point(user));
        point.addPoints(1000);
        pointRepository.save(point);
        PointChargeRequestDto requestDto = new PointChargeRequestDto(user.getId(), 500);

        PointResponseDto pointResponseDto = pointService.chargePoints(requestDto);

        assertNotNull(pointResponseDto);
        assertEquals(user.getId(), pointResponseDto.getUserId());
        assertEquals(1500, pointResponseDto.getPoints());
    }

    @Test
    public void 포인트_충전_실패_사용자_없음() {
        PointChargeRequestDto requestDto = new PointChargeRequestDto(999L, 500);

        BusinessException exception = assertThrows(BusinessException.class, () -> pointService.chargePoints(requestDto));
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    public void 포인트_충전_실패_잘못된_포인트() {
        User user = userRepository.save(new User("testuser@example.com", "password"));
        pointRepository.save(new Point(user));
        PointChargeRequestDto requestDto = new PointChargeRequestDto(user.getId(), -500);

        BusinessException exception = assertThrows(BusinessException.class, () -> pointService.chargePoints(requestDto));
        assertEquals(ErrorCode.INVALID_DECREASE_QUANTITY, exception.getErrorCode());
    }
}
