package gift.repository;

import gift.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findAll(Pageable pageable);

    boolean existsByCategoryId(Long categoryId);

    @Query("select p from Product p join p.options o where o.id = :optionId")
    Product findByOptionId(Long optionId);

    @Query("select case when count(p) > 0 then true else false end from Product p join p.options o where o.id = :optionId")
    boolean existsByOptionId(@Param("optionId") Long optionId);
}
