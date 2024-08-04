package gift.service;

import gift.model.category.Category;
import gift.model.category.CategoryDTO;
import gift.model.category.CategoryRequest;
import gift.exception.ResourceNotFoundException;
import gift.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    Long defaultCategoryId = -1L;

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public CategoryDTO findOne(Long id) {
        return new CategoryDTO(findById(id));
    }

    @Transactional
    public Category save(CategoryRequest categoryRequest) {
        Category category = new Category(categoryRequest);
        return categoryRepository.save(category);
    }

    @Transactional
    public Category update(Long id, CategoryRequest categoryRequest) {
        Category category = findById(id);
        if (category.getId() == -1L) {
            throw new ResourceNotFoundException("Category not found with id " + id);
        }
        category.setCategory(categoryRequest);
        return categoryRepository.save(category);
    }

    @Transactional
    public void delete(Long id) {
        Category category = findById(id);
        if (category.getId() == -1L) {
            throw new ResourceNotFoundException("Category not found with id " + id);
        }
        categoryRepository.delete(category);
    }

    public Category findById(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isEmpty()) {
            return categoryRepository.findById(-1L).get(); // return defaultCategory
        }
        return category.get();
    }
}
