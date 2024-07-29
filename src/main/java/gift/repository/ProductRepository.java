package gift.repository;

import gift.entity.Product;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>,
    PagingAndSortingRepository<Product, Long> {

    Optional<Product> findByName(String name);

}