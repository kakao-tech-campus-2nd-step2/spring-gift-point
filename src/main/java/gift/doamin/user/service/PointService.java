package gift.doamin.user.service;

import gift.doamin.user.dto.PointResponse;
import gift.doamin.user.entity.User;
import gift.doamin.user.exception.UserNotFoundException;
import gift.doamin.user.repository.JpaUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PointService {

    private final JpaUserRepository userRepository;

    public PointService(JpaUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public PointResponse getPoint(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return new PointResponse(user.getPoint());
    }

    @Transactional
    public PointResponse addPoint(Long userId, Integer point) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        user.addPoint(point);
        return new PointResponse(user.getPoint());
    }
}
