package gift.product.persistence.repository;


import gift.product.persistence.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJpaRepository extends JpaRepository<Product, Long>{

}
