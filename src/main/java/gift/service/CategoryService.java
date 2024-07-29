package gift.service;

import gift.domain.model.dto.CategoryAddRequestDto;
import gift.domain.model.dto.CategoryResponseDto;
import gift.domain.model.dto.CategoryUpdateRequestDto;
import gift.domain.model.entity.Category;
import gift.domain.repository.CategoryRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryResponseDto> getAllCategories() {
        return categoryRepository.findAll().stream()
            .map(CategoryResponseDto::toDto)
            .collect(Collectors.toList());
    }

    public CategoryResponseDto addCategory(CategoryAddRequestDto categoryAddRequestDto) {
        validateCategoryName(categoryAddRequestDto.getName());

        Category category = categoryAddRequestDto.toEntity();
        Category savedCategory = categoryRepository.save(category);

        return CategoryResponseDto.toDto(savedCategory);
    }

    public CategoryResponseDto updateCategory(Long id, CategoryUpdateRequestDto categoryUpdateRequestDto) {
        validateCategoryId(id);
        validateCategoryName(categoryUpdateRequestDto.getName());

        Category category = categoryUpdateRequestDto.toEntity();
        category.update(id, categoryUpdateRequestDto.getName());
        Category savedCategory = categoryRepository.save(category);

        return CategoryResponseDto.toDto(savedCategory);
    }

    public void deleteCategory(Long id) {
        validateCategoryId(id);
        categoryRepository.deleteById(id);
    }

    private void validateCategoryId(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new IllegalArgumentException("해당 카테고리가 존재하지 않습니다.");
        }
    }

    private void validateCategoryName(String name) {
        if (categoryRepository.existsByName(name)) {
            throw new IllegalArgumentException("이미 존재하는 카테고리입니다.");
        }
    }
}
