package gift.service;

import gift.model.Category;
import gift.dto.CategoryDto;
import gift.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

  @Autowired
  private CategoryRepository categoryRepository;

  public CategoryDto saveCategory(CategoryDto categoryDto) {
    Category category = convertToEntity(categoryDto);
    Category savedCategory = categoryRepository.save(category);
    return convertToDto(savedCategory);
  }

  public Category convertToEntity(CategoryDto categoryDto) {
    return new Category(categoryDto.getName(), categoryDto.getColor(), categoryDto.getImageUrl(), categoryDto.getDescription());
  }

  private CategoryDto convertToDto(Category category) {
    return new CategoryDto(category.getId(), category.getName(), category.getColor(), category.getImageUrl(), category.getDescription());
  }

  public List<CategoryDto> findAllCategories() {
    List<Category> categories = categoryRepository.findAll();
    return categories.stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
  }

}