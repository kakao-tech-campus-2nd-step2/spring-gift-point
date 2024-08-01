package gift.repository;

import gift.entity.Option;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OptionJpaDao extends JpaRepository<Option, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select o from Option o where o.id = :id")
    Optional<Option> findByIdForUpdate(@Param("id") Long id);
}
