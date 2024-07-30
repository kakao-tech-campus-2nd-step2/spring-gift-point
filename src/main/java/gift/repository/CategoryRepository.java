package gift.repository;

import gift.entity.CategoryEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    @Override
    Page<CategoryEntity> findAll(Pageable pageable);

    Optional<CategoryEntity> findByName(String name);
}
