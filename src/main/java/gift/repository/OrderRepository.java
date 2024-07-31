package gift.repository;

import gift.model.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    Page<Orders> findByMemberId(Long memberId, Pageable pageable);
}
