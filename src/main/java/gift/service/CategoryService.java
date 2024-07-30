package gift.service;

import gift.domain.Category;
import gift.dto.request.CategoryRequestDto;
import gift.dto.response.CategoryResponseDto;
import gift.exception.customException.EntityNotFoundException;
import gift.exception.customException.NameDuplicationException;
import gift.repository.category.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static gift.exception.exceptionMessage.ExceptionMessage.CATEGORY_NOT_FOUND;

@Service
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryResponseDto> findAllCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryResponseDto::from)
                .collect(Collectors.toList());
    }

    public CategoryResponseDto findOneCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(CATEGORY_NOT_FOUND));

        return CategoryResponseDto.from(category);
    }

    @Transactional
    public CategoryResponseDto saveCategory(CategoryRequestDto categoryRequestDto) {
        categoryRepository.findCategoryByName(categoryRequestDto.name())
                .ifPresent(e -> {
                    throw new NameDuplicationException();
                });

        Category category = new Category(categoryRequestDto.name(), categoryRequestDto.color());

        categoryRepository.save(category);

        return CategoryResponseDto.from(category);
    }

    @Transactional
    public CategoryResponseDto updateCategory(Long categoryId, CategoryRequestDto categoryRequestDto) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException(CATEGORY_NOT_FOUND));

        categoryRepository.findCategoryByName(categoryRequestDto.name())
                .ifPresent(e -> {
                    throw new NameDuplicationException();
                });

        category.update(categoryRequestDto);

        return CategoryResponseDto.from(category);
    }

    @Transactional
    public CategoryResponseDto deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException(CATEGORY_NOT_FOUND));

        categoryRepository.deleteById(categoryId);

        return CategoryResponseDto.from(category);
    }
}
