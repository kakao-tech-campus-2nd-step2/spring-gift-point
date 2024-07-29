package gift.service;

import gift.entity.Category;
import gift.exception.DataNotFoundException;
import gift.repository.CategoryRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    public Category findById(Long id) {
        return categoryRepository.findById(id)
            .orElseThrow(() -> new DataNotFoundException("존재하지 않는 Category"));
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public void save(Category category) {
        categoryRepository.save(category);
    }

}
