package gift.service;

import gift.domain.Category;
import gift.dto.CategoryDto;
import gift.repository.CategoryRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryDto> getAllCategories(){
        List<Category> categories = categoryRepository.findAll();

        return categories.stream()
            .map(CategoryDto::convertToDto)
            .collect(Collectors.toList());
    }


    public String addCategory(CategoryDto categoryDto) {
        Category category = new Category(categoryDto.getName(),
            categoryDto.getColor(),
            categoryDto.getImageUrl(),
            categoryDto.getDescription()
        );
        Category savedCategory = categoryRepository.save(category);

        return savedCategory.getName();
    }

    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("해당 id의 카테고리 없음: " + id));
        category.setName(categoryDto.getName());
        category.setColor(categoryDto.getColor());
        category.setImageUrl(categoryDto.getImageUrl());
        category.setDescription(categoryDto.getDescription());
        Category savedCategory = categoryRepository.save(category);

        return new CategoryDto(savedCategory.getId(), savedCategory.getName(),
            savedCategory.getColor(), savedCategory.getImageUrl(), savedCategory.getDescription());
    }

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
}
