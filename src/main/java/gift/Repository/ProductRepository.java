package gift.Repository;

import gift.Model.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findProductById(Long id);

    @Transactional
    void deleteByCategoryId(Long categoryId);

}
