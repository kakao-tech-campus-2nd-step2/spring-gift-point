package gift.repository;

import gift.model.Option;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OptionRepository extends JpaRepository<Option, Long> {
    List<Option> findByProductId(Long productId);
    boolean existsByNameAndProductId(String name, Long productId);
}