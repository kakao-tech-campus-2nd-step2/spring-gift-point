package gift.database;

import gift.model.Category;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCategoryRepository extends JpaRepository<Category, Long> {

    <S extends Category> S save(S entity);

    Optional<Category> findById(Long id);

    void delete(Category entity);

    List<Category> findAll();

}
