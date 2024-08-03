package gift.product.repository;

import gift.product.model.Order;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findAllByMemberId(Pageable pageable, Long memberId);

    Optional<Order> findByIdAndMemberId(Long id, Long memberId);

    void deleteByIdAndMemberId(Long id, Long memberId);
}
