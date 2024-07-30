package gift.repository;

import gift.dto.order.OrderResponseDTO;
import gift.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query("select new gift.dto.order.OrderResponseDTO(o.id, o.quantity, o.orderDateTime, o.message) from order_tb o where o.user.id = :userId")
    Page<OrderResponseDTO> findAllWithPage(int userId, Pageable pageable);
}
