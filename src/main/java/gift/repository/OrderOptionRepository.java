package gift.repository;

import gift.domain.OrderOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderOptionRepository extends JpaRepository<OrderOption, Long> {

}
