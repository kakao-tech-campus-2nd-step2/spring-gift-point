package gift.repository;

import gift.entity.Option;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface OptionRepository extends JpaRepository<Option,Long> {
    public Optional<Option> findByIdAndName(Long id, String name);

    public List<Option> getOptionByProductId(Long productId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public Option getOptionById(Long id);
}
