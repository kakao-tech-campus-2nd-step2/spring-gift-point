package gift.service;

import gift.common.exception.CategoryNotFoundException;
import gift.model.category.Category;
import gift.model.category.CategoryRequest;
import gift.model.category.CategoryResponse;
import gift.repository.category.CategoryRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryResponse> getAllCategory() {
        return categoryRepository.findAll().stream()
            .map(CategoryResponse::from)
            .collect(Collectors.toList());
    }

    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
            () -> new CategoryNotFoundException("해당 Id의 카테고리는 존재하지 않습니다."));
        return CategoryResponse.from(category);
    }

    public CategoryResponse addCategory(CategoryRequest categoryRequest) {
        Category category = toEntity(categoryRequest);
        return CategoryResponse.from(categoryRepository.save(category));
    }

    public CategoryResponse updateCategory(Long id, CategoryRequest categoryRequest) {
        Category category = categoryRepository.findById(id).orElseThrow(
            () -> new CategoryNotFoundException("해당 Id의 카테고리는 존재하지 않습니다.")
        );
        category.update(categoryRequest);
        return CategoryResponse.from(categoryRepository.save(category));
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }


    public Category toEntity(CategoryRequest categoryRequest) {
        return new Category(categoryRequest.name(), categoryRequest.color(),
            categoryRequest.imageUrl(), categoryRequest.description());
    }




}
