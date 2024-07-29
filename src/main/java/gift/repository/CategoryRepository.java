package gift.repository;

import gift.domain.Category;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    public Optional<Category> findByName(String name);
}
