package gift.service;

import gift.domain.Category;
import gift.dto.CategoryListDto;
import gift.dto.UpdateCategoryDto;
import gift.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    public final CategoryRepository categoryRepository;
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Page<CategoryListDto> findAll(Pageable pageable) {
        Page<Category> categories = categoryRepository.findAll(pageable);
        return categories.map(CategoryListDto::new);
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
