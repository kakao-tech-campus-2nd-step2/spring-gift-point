package gift.service;

import gift.dto.PointChargeRequestDto;
import gift.dto.PointResponseDto;
import gift.entity.Point;
import gift.entity.User;
import gift.exception.BusinessException;
import gift.exception.ErrorCode;
import gift.repository.PointRepository;
import gift.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PointService {

    private final PointRepository pointRepository;
    private final UserRepository userRepository;

    public PointService(PointRepository pointRepository, UserRepository userRepository) {
        this.pointRepository = pointRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public PointResponseDto getUserPoints(Long userId) {
        User user = findUserById(userId);
        Point point = pointRepository.findByUser(user)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        return new PointResponseDto(user.getId(), point.getPoints());
    }

    @Transactional
    public PointResponseDto chargePoints(PointChargeRequestDto requestDto) {
        User user = findUserById(requestDto.getUserId());
        Point point = pointRepository.findByUser(user)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        if (requestDto.getPoints() <= 0) {
            throw new BusinessException(ErrorCode.INVALID_DECREASE_QUANTITY);
        }
        point.addPoints(requestDto.getPoints());
        pointRepository.save(point);
        return new PointResponseDto(user.getId(), point.getPoints());
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }
}
