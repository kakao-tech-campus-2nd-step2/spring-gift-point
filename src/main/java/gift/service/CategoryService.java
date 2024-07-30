package gift.service;

import gift.dto.CategoryRequestDto;
import gift.dto.CategoryResponseDto;
import gift.entity.Category;
import gift.exception.BusinessException;
import gift.exception.ErrorCode;
import gift.mapper.CategoryMapper;
import gift.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryResponseDto addCategory(CategoryRequestDto categoryRequestDto) {
        Category category = new Category(
                categoryRequestDto.getName(),
                categoryRequestDto.getColor(),
                categoryRequestDto.getImageUrl(),
                categoryRequestDto.getDescription()
        );
        Category createdCategory = categoryRepository.save(category);
        return CategoryMapper.toCategoryResponseDto(createdCategory);
    }

    public CategoryResponseDto updateCategory(Long id, CategoryRequestDto categoryRequestDto) {
        Category existingCategory = getCategoryEntityById(id);
        existingCategory.update(
                categoryRequestDto.getName(),
                categoryRequestDto.getColor(),
                categoryRequestDto.getImageUrl(),
                categoryRequestDto.getDescription()
        );
        categoryRepository.save(existingCategory);
        return CategoryMapper.toCategoryResponseDto(existingCategory);
    }

    public List<CategoryResponseDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(CategoryMapper::toCategoryResponseDto)
                .collect(Collectors.toList());
    }

    public CategoryResponseDto getCategoryById(Long id) {
        Category category = getCategoryEntityById(id);
        return CategoryMapper.toCategoryResponseDto(category);
    }

    public void deleteCategory(Long id) {
        Category category = getCategoryEntityById(id);
        categoryRepository.delete(category);
    }

    public Category getCategoryEntityById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND, "ID: " + id));
    }
}
