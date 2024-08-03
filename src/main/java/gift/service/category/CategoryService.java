package gift.service.category;

import gift.domain.category.Category;
import gift.domain.category.CategoryRepository;
import gift.mapper.CategoryMapper;
import gift.web.dto.category.CategoryRequestDto;
import gift.web.dto.category.CategoryResponseDto;
import gift.web.exception.duplicate.CategoryDuplicatedException;
import gift.web.exception.notfound.CategoryNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public List<CategoryResponseDto> getCategories() {
        return categoryRepository.findAll()
            .stream()
            .map(categoryMapper::toDto)
            .toList();
    }

    public CategoryResponseDto getCategory(Long id) {
        return categoryMapper.toDto(categoryRepository.getOne(id));
    }

    @Transactional
    public CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto) {
        categoryRepository.findByName(categoryRequestDto.name())
            .ifPresent(category -> {
                throw new CategoryDuplicatedException();
            });

        Category category = categoryRepository.save(categoryMapper.toEntity(categoryRequestDto));
        return categoryMapper.toDto(category);
    }

    @Transactional
    public CategoryResponseDto updateCategory(Long id, CategoryRequestDto categoryRequestDto) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new CategoryNotFoundException());

        category.updateCategory(
            categoryRequestDto.name(),
            categoryRequestDto.color(),
            categoryRequestDto.description(),
            categoryRequestDto.imageUrl()
        );

        return categoryMapper.toDto(category);
    }

    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new CategoryNotFoundException());

        categoryRepository.delete(category);
    }
}