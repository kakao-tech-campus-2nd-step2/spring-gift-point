package gift.service;

import gift.entity.Category;
import gift.entity.CategoryDTO;
import gift.exception.ResourceNotFoundException;
import gift.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    Long defaultCategoryId = 1L;

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Transactional
    public Category save(CategoryDTO categoryDTO) {
        Category category = new Category(categoryDTO);
        return categoryRepository.save(category);
    }

    public Category findById(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isEmpty()) {
            return categoryRepository.findById(1L).get(); // return defaultCategory
        }
        return category.get();
    }

    @Transactional
    public Category update(Long id, CategoryDTO categoryDTO) {
        Category category = findById(id);
        if (category.getId() == 1L) {
            throw new ResourceNotFoundException("Category not found with id " + id);
        }
        category.setCategory(categoryDTO);
        return categoryRepository.save(category);
    }

    @Transactional
    public void delete(Long id) {
        Category category = findById(id);
        categoryRepository.delete(category);
    }
}
