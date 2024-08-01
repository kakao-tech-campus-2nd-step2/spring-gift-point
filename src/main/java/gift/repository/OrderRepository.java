package gift.repository;

import gift.entity.Order;
import gift.entity.Wish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByMemberId(Long memberId);
    Page<Order> findBymemberId(Long memberId, Pageable pageable);
}
