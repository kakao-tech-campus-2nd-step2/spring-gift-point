package gift.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import gift.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{
}
