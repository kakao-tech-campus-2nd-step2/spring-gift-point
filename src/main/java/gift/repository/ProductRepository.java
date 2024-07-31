package gift.repository;

import gift.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p JOIN FETCH p.category")
    Page<Product> findProductAndCategoryFetchJoin(Pageable pageable);

    @Query("SELECT p FROM Product p JOIN FETCH p.category JOIN FETCH p.options WHERE p.id= :id")
    Optional<Product> findAllByIdFetchJoin(Long id);

    @Query("SELECT p FROM Product p JOIN FETCH p.category c WHERE c.id = :categoryId")
    List<Product> findByCategoryId(Long categoryId);

    @Query("SELECT p FROM Product p JOIN FETCH p.options WHERE p.id= :id")
    Optional<Product> findProductAndOptionByIdFetchJoin(Long id);
}
