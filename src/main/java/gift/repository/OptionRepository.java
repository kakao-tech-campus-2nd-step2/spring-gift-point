package gift.repository;

import gift.model.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {
    List<Option> findAllByProduct_Id(Long productId);
    void deleteByName(String name);
    Optional<Option> findByName(String name);
    Optional<Option> findByNameAndProduct_Id(String name, Long productId);
    @Modifying
    @Transactional
    @Query("UPDATE Option o SET o.quantity = o.quantity + :increment WHERE o.product.id = :productId")
    void updateQuantityByProductId(Long productId, @Param("increment") long increment);
}
