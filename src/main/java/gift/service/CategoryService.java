package gift.service;

import gift.domain.Category;
import gift.dto.UpdateCategoryDto;
import gift.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    public final CategoryRepository categoryRepository;
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 카테고리가 없습니다."));
    }

    public Category addCategory(String name) {
        Category category = categoryRepository.findByName(name);
        if (category == null) {
            category = new Category(name);
            categoryRepository.save(category);
        }
        return category;
    }

    public Category updateCategory(Long id, UpdateCategoryDto updateCategoryDto) {
        Category category = findById(id);
        category.setName(updateCategoryDto.getName());
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        Category category = findById(id);
        categoryRepository.delete(category);
    }

}
