package gift.Repository;

import gift.Model.Entity.Member;
import gift.Model.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByMember(Member member);
}
