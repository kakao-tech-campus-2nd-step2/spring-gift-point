package gift.repository;


import gift.domain.Order;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
    Optional<List<Order>> findByMemberId(Long memberId);
    Page<Order> findByMemberId(Long memberId, Pageable pageable);
}
