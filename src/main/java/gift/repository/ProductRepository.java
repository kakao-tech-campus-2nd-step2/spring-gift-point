package gift.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gift.entity.Product;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByOrderByNameDesc(Pageable pageable);
    Optional<Product> findByNameAndPriceAndImageUrl(String name, int price, String imageUrl);
}