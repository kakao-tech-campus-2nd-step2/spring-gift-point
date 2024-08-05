package gift.user.service;

import gift.exception.CustomException;
import gift.exception.ErrorCode;
import gift.user.dto.request.ChargePointRequest;
import gift.user.dto.response.PointResponse;
import gift.user.entity.User;
import gift.user.repository.UserJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PointService {

    private static final Logger log = LoggerFactory.getLogger(PointService.class);
    private final UserJpaRepository userRepository;

    public PointService(UserJpaRepository userRepository) {
        this.userRepository = userRepository;
    }

    public PointResponse getPoint(User user) {
        return PointResponse.from(user);
    }

    @Transactional
    public PointResponse chargePoint(ChargePointRequest request) {
        log.info("Charge point request: {}", request);
        var user = userRepository.findById(request.userId())
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        user.chargePoint(request.point());
        return PointResponse.from(user);
    }

}
