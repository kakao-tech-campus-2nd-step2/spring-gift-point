package gift.Repository;

import gift.Entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OptionJpaRepository extends JpaRepository<Option, Long>{
    List<Option> findAllByProductId(long productId);
    void deleteById(long id);
}
