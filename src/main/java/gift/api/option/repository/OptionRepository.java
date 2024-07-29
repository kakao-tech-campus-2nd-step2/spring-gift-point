package gift.api.option.repository;

import gift.api.option.domain.Option;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select o from Option o where o.id = :id")
    Optional<Option> findByIdWithPessimisticWrite(Long id);

    List<Option> findAllByProductId(Long productId);
}
