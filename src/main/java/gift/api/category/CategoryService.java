package gift.api.category;

import gift.api.category.dto.CategoryRequest;
import gift.global.exception.NoSuchEntityException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryResponse> getCategories(Pageable pageable) {
        Page<Category> categories = categoryRepository.findAll(pageable);
        if (categories.hasContent()) {
            return categories.getContent()
                    .stream()
                    .map(CategoryResponse::of)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Transactional
    public Long add(CategoryRequest categoryRequest) {
        return categoryRepository.save(categoryRequest.toEntity()).getId();
    }

    @Transactional
    public void update(Long id, CategoryRequest categoryRequest) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new NoSuchEntityException("category"));
        category.update(categoryRequest.name(),
            categoryRequest.color(),
            categoryRequest.imageUrl(),
            categoryRequest.description());
    }
}
