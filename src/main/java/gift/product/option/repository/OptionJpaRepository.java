package gift.product.option.repository;

import gift.product.option.entity.Option;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface OptionJpaRepository extends JpaRepository<Option, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT o FROM Option o WHERE o.id = :id")
    Optional<Option> findByIdWithPessimisticLocking(Long id);

}
