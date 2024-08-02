package gift.repository;

import gift.model.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PointRepository extends JpaRepository<Point, Long>{
    Optional<Point> findByMember_Id(Long memberId);
}
