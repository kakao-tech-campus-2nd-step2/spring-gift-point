package gift.service;

import gift.exception.category.DuplicateCategoryException;
import gift.exception.category.NotFoundCategoryException;
import gift.model.Category;
import gift.repository.CategoryRepository;
import gift.response.category.CategoryResponse;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
            .map(CategoryResponse::createCategoryResponse)
            .toList();
    }

    public CategoryResponse getCategory(Long id) {
        return categoryRepository.findById(id)
            .map(CategoryResponse::createCategoryResponse)
            .orElseThrow(NotFoundCategoryException::new);
    }

    @Transactional
    public Category addCategory(String name, String color, String imageUrl, String description) {
        categoryRepository.findByName(name)
            .ifPresent(category -> {
                throw new DuplicateCategoryException();
            });

        return categoryRepository.save(new Category(name, color, imageUrl, description));
    }

    @Transactional
    public Category updateCategory(Long id, String name, String color, String imageUrl, String description) {
        return categoryRepository.findById(id)
            .map(category -> {
                category.updateCategory(name, color, imageUrl, description);
                return category;
            })
            .orElseThrow(NotFoundCategoryException::new);
    }

    @Transactional
    public void deleteCategory(Long id) {
        categoryRepository.findById(id)
            .map(category -> {
                categoryRepository.delete(category);
                return category.getId();
            })
            .orElseThrow(NotFoundCategoryException::new);
    }

}
