package gift.user.infra;

import gift.user.domain.UserPoint;

public interface UserPointRepository {
    UserPoint create(Long id);

    UserPoint findById(Long id);

    UserPoint addPoint(Long id, Long point);

    UserPoint findByUserId(Long userId);

    void updatePoint(Long userId, Long newPoint);
}
