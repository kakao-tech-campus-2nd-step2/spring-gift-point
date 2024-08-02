package gift.service;

import gift.domain.Category.CategoryRequest;
import gift.domain.Category.CategoryResponse;
import gift.entity.CategoryEntity;
import gift.error.AlreadyExistsException;
import gift.error.NotFoundException;
import gift.repository.CategoryRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    //카테고리 전체 조회 기능
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
            .map(CategoryResponse::from)
            .toList();
    }

    //단일 카테고리 조회 기능
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long id) {
        CategoryEntity categoryEntity = categoryRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Category not found"));
        return CategoryResponse.from(categoryEntity);

    }

    //카테고리 추가 기능
    @Transactional
    public void addCategory(CategoryRequest request) {
        validateCategoryUniqueness(request);
        CategoryEntity categoryEntity = new CategoryEntity(
            request.name(),
            request.color(),
            request.imageUrl(),
            request.description()
        );
        categoryRepository.save(categoryEntity);
    }

    //카테고리 수정 기능
    @Transactional
    public void updateCategory(Long id, CategoryRequest request) {
        validateCategoryUniqueness(request);
        CategoryEntity categoryEntity = categoryRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Category not found"));
        categoryEntity.updateCategoryEntity(
            request.name(),
            request.color(),
            request.imageUrl(),
            request.description()
        );
        categoryRepository.save(categoryEntity);
    }

    //카테고리 삭제 기능
    @Transactional
    public void deleteCategory(Long id) {
        CategoryEntity categoryEntity = categoryRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Category not found"));
        categoryRepository.delete(categoryEntity);
    }

    private void validateCategoryUniqueness(CategoryRequest request) {
        if(categoryRepository.existsByNameAndColor(request.name(),request.color())) {
            throw new AlreadyExistsException("Already Exists Category");
        }
    }

}
