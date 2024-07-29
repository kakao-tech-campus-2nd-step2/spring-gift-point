package gift.service;

import gift.exception.ErrorCode;
import gift.domain.Category;
import gift.dto.CategoryDto;
import gift.exception.GiftException;
import gift.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> getCategories() {
        return categoryRepository.findAll().stream()
                .map(Category::toDto)
                .toList();
    }

    public void addCategory(CategoryDto dto) {
        if (categoryRepository.existsByName(dto.getName())) {
            throw new GiftException(ErrorCode.CATEGORY_NAME_NOT_DUPLICATES);
        }

        Category category = new Category(dto.getName());

        categoryRepository.save(category);
    }

    public void editCategory(Long categoryId, CategoryDto dto) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new GiftException(ErrorCode.CATEGORY_NOT_FOUND));

        if (!category.getName().equals(dto.getName()) && categoryRepository.existsByName(dto.getName())) {
            throw new GiftException(ErrorCode.CATEGORY_NAME_NOT_DUPLICATES);
        }

        category.changeName(dto.getName());
    }

    public void removeCategory(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }

}
