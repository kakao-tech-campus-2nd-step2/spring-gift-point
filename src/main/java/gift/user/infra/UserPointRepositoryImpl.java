package gift.user.infra;

import gift.user.domain.UserPoint;
import org.springframework.stereotype.Repository;

@Repository
public class UserPointRepositoryImpl implements UserPointRepository {
    private final UserPointJpaRepository userPointJpaRepository;

    public UserPointRepositoryImpl(UserPointJpaRepository userPointJpaRepository) {
        this.userPointJpaRepository = userPointJpaRepository;
    }

    public UserPoint create(Long id) {
        userPointJpaRepository.save(new UserPoint(id));
        return null;
    }

    public UserPoint findById(Long id) {
        return userPointJpaRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("UserPoint not found")
        );
    }

    public UserPoint addPoint(Long id, Long point) {
        UserPoint userPoint = findById(id);
        userPoint.addPoint(point);
        userPointJpaRepository.save(userPoint);
        return userPoint;
    }

    public UserPoint findByUserId(Long userId) {
        return userPointJpaRepository.findByUserId(userId).orElseThrow(
                () -> new IllegalArgumentException("UserPoint not found")
        );
    }

    public void updatePoint(Long userId, Long newPoint) {
        UserPoint userPoint = findByUserId(userId);
        userPoint.updatePoint(newPoint);
        userPointJpaRepository.save(userPoint);
    }
}
