package gift.product.infra;

import gift.product.domain.Category;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategoryRepository {

    private final CategoryJpaRepository categoryJpaRepository;

    public CategoryRepository(CategoryJpaRepository categoryJpaRepository) {
        this.categoryJpaRepository = categoryJpaRepository;
    }

    public Category findById(Long id) {
        return categoryJpaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 카테고리가 존재하지 않습니다."));
    }

    public Category save(Category category) {
        return categoryJpaRepository.save(category);
    }

    public void deleteById(Long id) {
        categoryJpaRepository.deleteById(id);
    }

    public Category findByName(String name) {
        return categoryJpaRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("해당 이름의 카테고리가 존재하지 않습니다."));
    }

    public List<Category> findAll() {
        return categoryJpaRepository.findAll();
    }
}
