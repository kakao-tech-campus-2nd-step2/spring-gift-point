package gift.service;

import gift.dto.CategoryDto;
import gift.entity.Category;
import gift.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryDto> findAll() {
        return categoryRepository.findAll().stream()
                .map(category -> new CategoryDto(
                        category.getId(),
                        category.getName(),
                        category.getColor(),
                        category.getImgUrl(),
                        category.getDescription()))
                .collect(Collectors.toList());
    }
}
