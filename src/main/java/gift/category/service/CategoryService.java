package gift.category.service;

import gift.category.dto.CategoryDto;
import gift.category.repository.CategoryRepository;
import gift.exception.ResourceNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

  private final CategoryRepository categoryRepository;

  public CategoryService(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  public List<CategoryDto> getAllCategories() {
    List<CategoryDto> categories = categoryRepository.findAll().stream().map(CategoryDto::toDto)
        .toList();

    if (categories.isEmpty()) {
      throw new ResourceNotFoundException("카테고리를 찾을 수 없습니다.");
    }

    return categories;
  }

}
