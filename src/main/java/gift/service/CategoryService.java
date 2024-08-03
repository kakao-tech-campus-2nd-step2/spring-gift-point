package gift.service;

import gift.dto.CategoryRequest;
import gift.dto.CategoryResponse;
import gift.entity.Category;
import gift.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryResponse> findAll() {
        return categoryRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public CategoryResponse findById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category with id " + id + " not found"));
        return convertToResponse(category);
    }

    public CategoryResponse save(CategoryRequest categoryRequest) {
        Category category = new Category();
        category.setName(categoryRequest.name());
        category.setColor(categoryRequest.color());
        category.setImageUrl(categoryRequest.imageUrl());
        category.setDescription(categoryRequest.description());

        Category savedCategory = categoryRepository.save(category);
        return convertToResponse(savedCategory);
    }

    public void delete(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category with id " + id + " not found"));
        categoryRepository.delete(category);
    }

    private CategoryResponse convertToResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getColor(),
                category.getImageUrl(),
                category.getDescription()
        );
    }
}
