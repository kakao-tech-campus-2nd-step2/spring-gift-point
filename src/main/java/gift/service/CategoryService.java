package gift.service;

import gift.domain.CategoryDTO;
import gift.entity.Category;
import gift.repository.CategoryRepository;
import jakarta.persistence.PersistenceException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> findAll() {
        try {
            return categoryRepository.findAll();
        } catch (PersistenceException e) {
            throw new PersistenceException("Could not find all categories");
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error");
        }
    }

    public Optional<Category> findById(int id) {
        var category = categoryRepository.findById(id);

        if (category == null) {
            return Optional.empty();
        }
        return category;
    }

    public Category add(CategoryDTO categoryDTO) {
        Category category = new Category(categoryDTO.name(), categoryDTO.color(), categoryDTO.imageUrl(), categoryDTO.description());
        try {
            return categoryRepository.save(category);
        } catch (PersistenceException e) {
            throw new PersistenceException("Could not find all categories");
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error");
        }
    }

    public Optional<Category> update(int id, CategoryDTO categoryDTO) {
        var category = categoryRepository.findById(id);
        if (category == null) {
            return Optional.empty();
        }
        return Optional.of(categoryRepository.save(new Category(id, categoryDTO.name(), categoryDTO.color(), categoryDTO.imageUrl(), categoryDTO.description())));
    }

    public void delete(Integer id) {
        try {
            categoryRepository.deleteById(id);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("There is no Category with id:" + id);
        } catch (PersistenceException e) {
            throw new PersistenceException("Can't delete category " + id);
        } catch (Exception e) {
            throw new RuntimeException("Can't delete category " + id);
        }
    }
}
