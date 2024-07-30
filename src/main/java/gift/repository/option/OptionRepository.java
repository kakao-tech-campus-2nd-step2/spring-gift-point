package gift.repository.option;

import gift.domain.Option;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OptionRepository extends JpaRepository<Option, Long> {

    Optional<Option> findOptionByNameAndProductId(String name, Long productId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select o from Option o where o.id = :id")
    Optional<Option> findOptionByIdForUpdate(@Param("id") Long id);

    List<Option> findOptionsByProductId(Long productId);

    Long countOptionByProductId(Long productId);


}
