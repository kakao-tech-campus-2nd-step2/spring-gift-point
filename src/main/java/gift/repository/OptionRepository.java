package gift.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gift.entity.Option;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long>{

	List<Option> findByProductId(Long productId);
	Optional<Option> findByProductIdAndName(Long productId, String name);
}
