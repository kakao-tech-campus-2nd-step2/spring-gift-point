package gift.service.category;

import gift.domain.category.Category;
import gift.domain.category.CategoryRepository;
import gift.mapper.CategoryMapper;
import gift.web.dto.CategoryDto;
import gift.web.exception.CategoryNotFoundException;
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

    public List<CategoryDto> getCategories() {
        return categoryRepository.findAll()
            .stream()
            .map(categoryMapper::toDto)
            .toList();
    }

    public CategoryDto getCategory(Long id) {
        return categoryMapper.toDto(categoryRepository.getOne(id));
    }

    @Transactional
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = categoryRepository.save(categoryMapper.toEntity(categoryDto));
        return categoryMapper.toDto(category);
    }

    @Transactional
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new CategoryNotFoundException("카테고리가 없슴다."));

        category.updateCategory(
            categoryDto.name(),
            categoryDto.color(),
            categoryDto.description(),
            categoryDto.imageUrl()
        );

        return categoryMapper.toDto(category);
    }

    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new CategoryNotFoundException("카테고리가 없슴다."));

        categoryRepository.delete(category);
    }
}