package gift.user.application;

import gift.user.infra.UserPointRepository;
import org.springframework.stereotype.Service;

@Service
public class PointService {
    private final UserPointRepository userPointRepository;

    public PointService(UserPointRepository userPointRepository) {
        this.userPointRepository = userPointRepository;
    }

    public Long getPoint(Long userId) {
        return userPointRepository.findByUserId(userId).getPoint();
    }

    public Long userPoint(Long userId, Long point) {
        Long currentPoint = userPointRepository.findByUserId(userId).getPoint();
        Long newPoint = currentPoint - point;
        if (newPoint < 0) {
            throw new IllegalArgumentException("포인트가 부족합니다.");
        }
        userPointRepository.updatePoint(userId, newPoint);
        return newPoint;
    }
}
