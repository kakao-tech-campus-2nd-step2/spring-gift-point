package gift.repository;

import gift.model.Option;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {
    List<Option> findAllByProductId(Long productId, Pageable pageable);

    boolean existsOptionByProductIdAndName(Long productId, String name);

    void deleteAllByProductId(Long productId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select o from Option o where o.id = :id")
    Optional<Option> findByIdWithLock(Long id);
}
