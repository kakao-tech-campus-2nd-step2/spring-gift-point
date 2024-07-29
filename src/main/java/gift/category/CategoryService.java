package gift.category;

import gift.category.dto.CategoryResponseDTO;
import gift.category.entity.Category;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> getAllCategories() {
        return categoryRepository.findAll()
            .stream()
            .map(category -> new CategoryResponseDTO(
                category.getName()
            ))
            .toList();
    }

}
