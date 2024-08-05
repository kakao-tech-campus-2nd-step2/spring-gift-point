package gift.user.service;

import gift.core.domain.order.PointRepository;
import gift.core.domain.user.User;
import gift.core.domain.user.UserRepository;
import gift.core.domain.user.exception.UserNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class MeService {
    private final UserRepository userRepository;
    private final PointRepository pointRepository;

    public MeService(UserRepository userRepository, PointRepository pointRepository) {
        this.userRepository = userRepository;
        this.pointRepository = pointRepository;
    }

    @Transactional
    public UserInfoDto getUserInfo(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Long remainPoint = pointRepository.getPoint(userId);

        return UserInfoDto.of(user, remainPoint);
    }
}
