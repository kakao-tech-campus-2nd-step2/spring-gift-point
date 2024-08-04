package gift.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import gift.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>{
    List<Order> findByMemberId(Long memberId);
    Optional<Order> findByMemberIdAndOptionId(Long memberId, Long optionId);
}
