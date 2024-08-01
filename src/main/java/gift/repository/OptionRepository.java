package gift.repository;

import gift.model.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {
  boolean existsByNameAndProductId(String name, Long productId);
  List<Option> findByProductId(Long productId);
}
