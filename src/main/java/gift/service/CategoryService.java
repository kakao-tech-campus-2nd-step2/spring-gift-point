package gift.service;

import gift.dto.response.CategoryResponse;
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

    public Category getCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream().map(CategoryResponse::new)
                .collect(Collectors.toList());
    }



}
