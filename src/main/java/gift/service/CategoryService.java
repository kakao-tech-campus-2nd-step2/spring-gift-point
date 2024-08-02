package gift.service;

import gift.constants.Messages;
import gift.domain.Category;
import gift.dto.CategoryResponseDto;
import gift.dto.ProductRequestDto;
import gift.exception.CategoryNotFoundException;
import gift.repository.CategoryRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public void save(Category category) {
        categoryRepository.save(category);
    }

    public List<CategoryResponseDto> findAll() {
        return categoryRepository.findAll()
            .stream()
            .map(this::convertToCategoryResponseDto)
            .collect(Collectors.toList());
    }

    public Category findById(Long categoryId) {
        return categoryRepository.findById(categoryId)
            .orElseThrow(() -> new CategoryNotFoundException(
                Messages.NOT_FOUND_CATEGORY_MESSAGE));
    }

    private CategoryResponseDto convertToCategoryResponseDto(Category category) {
        return new CategoryResponseDto(
            category.getId(),
            category.getName(),
            category.getColor(),
            category.getImageUrl(),
            category.getDescription()
        );
    }

}