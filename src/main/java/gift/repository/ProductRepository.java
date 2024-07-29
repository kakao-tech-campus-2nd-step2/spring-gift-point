package gift.repository;


import gift.entity.Category;
import gift.entity.Product;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByIdAndNameAndPriceAndImageUrlAndCategoryName(Long id, String name, Integer price, String imageUrl, String categoryName);
    Optional<Product> findByIdAndNameAndPriceAndImageUrlAndCategory(Long id, String name, Integer price, String imageUrl, Category category);
    Boolean existsByCategoryId(Long categoryId);
}
