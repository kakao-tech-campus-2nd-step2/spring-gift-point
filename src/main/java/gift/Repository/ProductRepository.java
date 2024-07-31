package gift.Repository;

import gift.Model.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findProductById(Long id);

    Page<Product> findByCategoryId(Pageable pageable, Long id);

    @Transactional
    void deleteByCategoryId(Long categoryId);
}
