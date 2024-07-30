package gift.product.persistence.repository;


import gift.product.persistence.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJpaRepository extends JpaRepository<Product, Long>{

    Page<Product> findAllByCategoryId(Long categoryId, Pageable pageRequest);
}
