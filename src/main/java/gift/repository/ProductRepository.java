package gift.repository;

import gift.domain.Product;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    Optional<Product> findByNameAndPriceAndImageUrl(String name, Long price, String imageUrl);

    Page<Product> findAll(Pageable pageable);
}