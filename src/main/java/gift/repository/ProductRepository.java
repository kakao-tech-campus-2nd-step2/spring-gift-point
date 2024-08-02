package gift.repository;

import gift.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 상품 데이터에 대한 데이터베이스 처리를 담당하는 인터페이스
 */
@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

}