package gift.category.service;

import gift.category.dto.CategoryReqDto;
import gift.category.dto.CategoryResDto;
import gift.category.entity.Category;
import gift.category.exception.CategoryAlreadyExistsException;
import gift.category.exception.CategoryCreateException;
import gift.category.exception.CategoryDeleteException;
import gift.category.exception.CategoryNotFoundException;
import gift.category.exception.CategoryUpdateException;
import gift.category.repository.CategoryRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<CategoryResDto> getCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(CategoryResDto::new)
                .toList();
    }

    @Transactional
    public void addCategory(CategoryReqDto categoryReqDto) {
        checkDuplicateCategoryByName(categoryReqDto.name());

        Category category = new Category(categoryReqDto.name(), categoryReqDto.color(), categoryReqDto.imageUrl(), categoryReqDto.description());
        try {
            categoryRepository.save(category);
        } catch (Exception e) {
            throw CategoryCreateException.EXCEPTION;
        }
    }

    private void checkDuplicateCategoryByName(String name) {
        if (categoryRepository.existsByName(name)) {
            throw CategoryAlreadyExistsException.EXCEPTION;
        }
    }

    @Transactional
    public void updateCategory(Long categoryId, CategoryReqDto categoryReqDto) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> CategoryNotFoundException.EXCEPTION
        );
        try {
            category.update(categoryReqDto.name(), categoryReqDto.color(), categoryReqDto.imageUrl(), categoryReqDto.description());
        } catch (Exception e) {
            throw CategoryUpdateException.EXCEPTION;
        }
    }

    @Transactional
    public void deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> CategoryNotFoundException.EXCEPTION
        );
        try {
            categoryRepository.delete(category);
        } catch (Exception e) {
            throw CategoryDeleteException.EXCEPTION;
        }
    }
}
