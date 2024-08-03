package gift.service;

import gift.domain.model.dto.PointResponseDto;
import gift.domain.model.entity.User;
import gift.domain.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
public class PointService {

    private final UserRepository userRepository;

    public PointService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public PointResponseDto getPoints(User user) {
        Integer totalPoints = userRepository.getPointBalance(user);
        return new PointResponseDto(totalPoints);
    }
}
