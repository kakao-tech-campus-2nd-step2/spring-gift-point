package gift.Repository;

import gift.Model.Option;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface OptionRepository extends JpaRepository<Option, Long> {
    @Query("SELECT option FROM Option option WHERE option.product.id = :id")
    List<Option> findAllById(@Param("id") Long id);

    Option findOptionById(Long id);

    @Transactional
    void deleteByProductId(Long productId);

}
