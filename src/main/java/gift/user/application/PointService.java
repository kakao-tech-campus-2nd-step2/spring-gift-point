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
}
