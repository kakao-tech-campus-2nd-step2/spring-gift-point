package gift.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import gift.entity.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>{
    List<Order> findByMemberId(Long memberId);
}
