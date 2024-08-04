package gift.option.domain;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OptionRepository extends JpaRepository<Option, Long> {

    List<Option> findAllByProductId(Long productId);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT o FROM Option o WHERE o.id = :id")
    Optional<Option> findByIdWithLock(@Param("id") Long id);
}
