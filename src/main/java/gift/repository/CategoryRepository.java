package gift.repository;

import gift.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 카테고리 데이터에 대한 데이터베이스 처리를 담당하는 인터페이스
 */
@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

}