package gift.product.repository;

import gift.product.model.Member;
import gift.product.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findAllByMember(Pageable pageable, Member member);
}
