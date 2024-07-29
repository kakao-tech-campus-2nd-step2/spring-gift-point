package gift.service;

import gift.domain.Category;
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
    public List<Category> getAllCategories() {
        return categoryRepository.findAll().stream()
            .map(CategoryEntity::toDto)
            .toList();
    }

    //단일 카테고리 조회 기능
    @Transactional(readOnly = true)
    public Category getCategoryById(Long id) {
        CategoryEntity categoryEntity = categoryRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Category not found"));
        return CategoryEntity.toDto(categoryEntity);

    }

    //카테고리 추가 기능
    @Transactional
    public void addCategory(Category category) {
        validateCategoryUniqueness(category);
        CategoryEntity categoryEntity = new CategoryEntity(
            category.getName(),
            category.getColor(),
            category.getImageUrl(),
            category.getDescription()
        );
        categoryRepository.save(categoryEntity);
    }

    //카테고리 수정 기능
    @Transactional
    public void updateCategory(Long id, Category category) {
        validateCategoryUniqueness(category);
        CategoryEntity categoryEntity = categoryRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Category not found"));
        categoryEntity.setName(category.getName());
        categoryEntity.setColor(category.getColor());
        categoryEntity.setImageUrl(category.getImageUrl());
        categoryEntity.setDescription(category.getDescription());
        categoryRepository.save(categoryEntity);
    }

    //카테고리 삭제 기능
    @Transactional
    public void deleteCategory(Long id) {
        CategoryEntity categoryEntity = categoryRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Category not found"));
        categoryRepository.delete(categoryEntity);
    }

    private void validateCategoryUniqueness(Category category) {
        if(categoryRepository.existsByNameAndColor(category.getName(),category.getColor())) {
            throw new AlreadyExistsException("Already Exists Category");
        }
    }

}
