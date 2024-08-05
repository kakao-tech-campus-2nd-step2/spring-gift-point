package gift.repository;


import gift.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderRepository extends JpaRepository<OrderItem, Long> {
    OrderItem findByOptionId(Long optionId);
}