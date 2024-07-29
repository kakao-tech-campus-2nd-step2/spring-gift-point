package gift.domain.product;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface JpaProductRepository extends JpaRepository<Product, Long> {

    @Modifying
    @Query(value = "DELETE FROM cart_item WHERE product_id = :productId; " +
                   "DELETE FROM option WHERE product_id = :productId;" +
                   "DELETE FROM product WHERE id = :productId; "
        , nativeQuery = true)
    void deleteById(Long productId);

    boolean existsByName(String name);

    @Transactional
    void deleteAllByIdIn(List<Long> productIds);

    List<Product> findAll();

    // paging
    Page<Product> findAll(Pageable pageable);

    Page<Product> findAllByIdIn(List<Long> cartItemIds, Pageable pageable);

    Optional<Product> findByName(String productName);
}
