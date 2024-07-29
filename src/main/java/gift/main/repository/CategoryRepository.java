package gift.main.repository;

import gift.main.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);

    Optional<Category> findByUniNumber(int uniNumber);

    boolean existsByName(String name);

    boolean existsByUniNumber(int uniNumber);
}
