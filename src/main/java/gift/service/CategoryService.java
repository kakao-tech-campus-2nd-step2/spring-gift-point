package gift.service;

import gift.dto.CategoryRequest;
import gift.dto.CategoryResponse;
import gift.entity.Category;
import gift.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    public CategoryResponse addCategory(CategoryRequest categoryRequest) {
        Category category = CategoryRequest.toEntity(categoryRequest);

        Category savedCategory = categoryRepository.save(category);

        return CategoryResponse.from(savedCategory);
    }

    public CategoryResponse updateCategory(Long id, CategoryRequest categoryRequest) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));

        category.updateCategory(categoryRequest.getName(), categoryRequest.getColor(),
            categoryRequest.getImgUrl(), categoryRequest.getDescription());

        Category updatedCategory = categoryRepository.save(category);

        return CategoryResponse.from(updatedCategory);
    }

    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

}
