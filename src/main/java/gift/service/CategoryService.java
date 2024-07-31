package gift.service;

import gift.domain.Category;
import gift.dto.CategoryDto;
import gift.repository.CategoryRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> getAllCategories(){
        List<Category> categories = categoryRepository.findAll();

        return categories.stream()
            .map(CategoryDto::convertToDto)
            .collect(Collectors.toList());
    }

    @Transactional
    public String addCategory(CategoryDto categoryDto) {
        Category category = new Category(categoryDto.name(),
            categoryDto.color(),
            categoryDto.imageUrl(),
            categoryDto.description()
        );
        Category savedCategory = categoryRepository.save(category);

        return savedCategory.getName();
    }

    @Transactional
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("해당 id의 카테고리 없음: " + id));
        category.setName(categoryDto.name());
        category.setColor(categoryDto.color());
        category.setImageUrl(categoryDto.imageUrl());
        category.setDescription(categoryDto.description());
        Category savedCategory = categoryRepository.save(category);

        return new CategoryDto(savedCategory.getId(), savedCategory.getName(),
            savedCategory.getColor(), savedCategory.getImageUrl(), savedCategory.getDescription());
    }

    @Transactional
    public String deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("해당 id의 카테고리 없음: " + id));

        String categoryName = category.getName();
        categoryRepository.delete(category);

        boolean exists = categoryRepository.existsById(id);
        if (exists) {
            throw new IllegalStateException("카테고리가 삭제되지 않았습니다: " + id);
        }

        return categoryName;
    }

    public Long findIdByName(String categoryName) {
        Category category = categoryRepository.findByName(categoryName);
        return category.getId();
    }
}
