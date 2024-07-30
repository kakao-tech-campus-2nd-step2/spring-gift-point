package gift.option;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionRepository extends JpaRepository<Option,Long> {

    Optional<Option> findByName(String name);

    List<Option> findAllByProductId(Long productId);

}
