package gift.product.repository;

import gift.product.model.Order;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByMemberId(Long memberId);

    Optional<Order> findByIdAndMemberId(Long id, Long memberId);

    void deleteByIdAndMemberId(Long id, Long memberId);
}
