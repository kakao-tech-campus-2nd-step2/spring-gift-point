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
    @Query(value = "DELETE FROM wish WHERE product_id = :productId; " + // 해당 상품을 포함하는 장바구니 정보 삭제
                   "DELETE FROM option WHERE product_id = :productId;" + // 해당 상품의 옵션 정보 삭제
                   "DELETE FROM product WHERE id = :productId; " // 상품 삭제
        , nativeQuery = true)
    void deleteById(Long productId);

    boolean existsByName(String name);

    @Transactional
    void deleteAllByIdIn(List<Long> productIds);

    List<Product> findAll();

    Optional<Product> findByName(String productName);


    // paging
    Page<Product> findAllByCategoryId(Long categoryId, Pageable pageable);
    Page<Product> findAll(Pageable pageable);
}

