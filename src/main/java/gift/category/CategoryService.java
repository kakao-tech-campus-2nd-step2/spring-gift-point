package gift.category;

import gift.category.dto.CategoryListResponseDTO;
import gift.category.dto.CategoryResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public CategoryListResponseDTO getAllCategories() {
        return new CategoryListResponseDTO(
            categoryRepository.findAll()
                .stream()
                .map(category -> new CategoryResponseDTO(
                    category.getId(),
                    category.getName(),
                    category.getColor(),
                    category.getImageUrl(),
                    category.getDescription()
                ))
                .toList()
        );
    }

}
