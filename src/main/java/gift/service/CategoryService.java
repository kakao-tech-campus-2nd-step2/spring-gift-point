package gift.service;

import gift.DTO.category.CategoryRequest;
import gift.DTO.category.CategoryResponse;
import gift.domain.Category;
import gift.exception.category.DuplicateCategoryNameException;
import gift.repository.CategoryRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();

        return categories.stream()
            .map(CategoryResponse::fromEntity)
            .toList();
    }

    @Transactional(readOnly = true)
    protected Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("No such category"));
    }

    @Transactional
    public CategoryResponse addCategory(CategoryRequest categoryRequest) {
        categoryRepository.findByName(categoryRequest.getName())
            .ifPresent(cat -> {
                throw new DuplicateCategoryNameException();
            });
        Category category = categoryRequest.toEntity();
        Category savedCategory = categoryRepository.save(category);

        return CategoryResponse.fromEntity(savedCategory);
    }

    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest categoryRequst) {
        Category category = categoryRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("No such category"));
        category.setName(categoryRequst.getName());
        category.setColor(categoryRequst.getColor());
        category.setImageUrl(categoryRequst.getImageUrl());
        category.setDescription(categoryRequst.getDescription());

        Category savedCategory = categoryRepository.save(category);

        return CategoryResponse.fromEntity(savedCategory);
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("No such category"));
        categoryRepository.delete(category);
    }
}
