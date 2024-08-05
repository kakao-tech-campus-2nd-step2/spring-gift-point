package gift.domain.order;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findAllByMemberId(Long id, PageRequest pageRequest);
}
