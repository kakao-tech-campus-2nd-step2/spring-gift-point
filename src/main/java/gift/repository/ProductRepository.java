package gift.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gift.entity.Product;
import java.util.Optional;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryIdOrderByNameAsc(Long categoryId);
    List<Product> findByCategoryIdOrderByNameDesc(Long categoryId);
    List<Product> findByCategoryIdOrderByPriceAsc(Long categoryId);
    List<Product> findByCategoryIdOrderByPriceDesc(Long categoryId);
    Page<Product> findByOrderByNameDesc(Pageable pageable);
    Optional<Product> findByNameAndPriceAndImageUrl(String name, int price, String imageUrl);
}