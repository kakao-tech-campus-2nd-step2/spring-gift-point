package gift.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gift.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{

	
}
