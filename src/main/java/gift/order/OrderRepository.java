package gift.order;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderInfo, Long> {

    List<OrderInfo> findByMemberId(Long memberId);
}
