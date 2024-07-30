package gift.repository.product;

import gift.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(
            "select p from Product p" +
                    " join fetch p.wishList" +
                    " where p.id = :id"
    )
    Optional<Product> findProductWithRelation(@Param("id") Long id);

    Page<Product> findAll(Pageable pageable);

}
