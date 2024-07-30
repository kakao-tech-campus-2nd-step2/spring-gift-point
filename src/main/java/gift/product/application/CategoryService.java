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
        if (categoryRepository.findByName(request.getName()) != null) {
            throw new IllegalArgumentException("이미 존재하는 카테고리입니다.");
        }
        Category category = new Category(request);

        categoryRepository.save(category);
    }

    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    public Category findByName(String name) {
        return categoryRepository.findByName(name);
    }

    public List<Category> getCategory() {
        return categoryRepository.findAll();
    }

    public Category getCategoryByName(String category) {
        return Optional.ofNullable(categoryRepository.findByName(category)).orElseThrow(() -> new IllegalArgumentException("해당 이름의 카테고리가 존재하지 않습니다."));
    }
}
