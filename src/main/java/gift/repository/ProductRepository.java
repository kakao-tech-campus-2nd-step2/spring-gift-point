package gift.repository;

import gift.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findById(int Id);
    List<Product> findAll();
    @Query("SELECT p FROM Product p where p.id BETWEEN :startId AND :endId")
    List<Product> findByProductIdBetween(@Param("startId") int startId, @Param("endId") int endId);
    Product save(Product product);
    void deleteById(int Id);
    Optional<Integer> searchCategory_IdById(int product_id);
    Optional<Integer> searchProduct_IdByCategory_Id(int category_id);
}