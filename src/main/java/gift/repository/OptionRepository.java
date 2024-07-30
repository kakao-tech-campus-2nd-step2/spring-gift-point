package gift.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gift.entity.Option;
import java.util.Optional;
import java.util.List;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {
    Optional<Option> findByName(String name);
    List<Option> findByProductId(Long productId);
}
