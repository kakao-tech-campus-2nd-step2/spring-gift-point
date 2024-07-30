package gift.product.application;

import gift.product.domain.Category;
import gift.product.domain.CreateCategoryRequest;
import gift.product.infra.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id);
    }

    public void addCategory(CreateCategoryRequest request) {

        if (categoryRepository.findByName(request.getName()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 카테고리입니다.");
        }
        Category category = new Category(request);

        categoryRepository.save(category);
    }

    public List<Category> getCategory() {
        return categoryRepository.findAll();
    }

    public Category getCategoryByName(String category) {
        return categoryRepository.findValidCategoryByName(category);
    }
}
