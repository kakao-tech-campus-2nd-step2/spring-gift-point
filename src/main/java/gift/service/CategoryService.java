package gift.service;

import gift.dto.CategoryRequest;
import gift.exception.NotFoundException;
import gift.model.Category;
import gift.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    @Transactional
    public Category update(CategoryRequest request) {
        Category category = categoryRepository.findById(request.id())
                .orElseThrow(() -> new NotFoundException("해당 카테고리가 존재하지 않습니다."));
        category.updateCategory(
                request.name(),
                request.color(),
                request.imageUrl(),
                request.description()
        );
        return category;
    }
}
