package gift.repository;

import gift.dto.category.CategoryDTO;
import gift.entity.Category;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Query("select new gift.dto.category.CategoryDTO(c.id,c.name) from Category c")
    Page<CategoryDTO> findAllCategory(Pageable pageable);

    Optional<Category> findByName(String name);
}
