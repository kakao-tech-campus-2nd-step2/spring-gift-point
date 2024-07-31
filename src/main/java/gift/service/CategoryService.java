package gift.service;

import gift.exception.ErrorCode;
import gift.domain.Category;
import gift.dto.CategoryDto;
import gift.exception.GiftException;
import gift.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static gift.exception.ErrorCode.CATEGORY_NOT_FOUND;

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

        categoryRepository.save(dto.toEntity());
    }

    public void editCategory(Long categoryId, CategoryDto dto) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new GiftException(CATEGORY_NOT_FOUND));

        if (!category.getName().equals(dto.getName()) && categoryRepository.existsByName(dto.getName())) {
            throw new GiftException(ErrorCode.CATEGORY_NAME_NOT_DUPLICATES);
        }

        category.changeName(dto.getName());
        category.changeColor(dto.getColor());
        category.changeImageUrl(dto.getImageUrl());
        category.changeDescription(dto.getDescription());
    }

    public void removeCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new GiftException(CATEGORY_NOT_FOUND));

        categoryRepository.delete(category);
    }

}
