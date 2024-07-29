package gift.repository;

import gift.model.Option;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OptionRepository extends JpaRepository<Option, Long> {

    @Query("select o from Option o join o.product p where p.id = :productId")
    Page<Option> findAllByProductId(Long productId, Pageable pageable);
}
