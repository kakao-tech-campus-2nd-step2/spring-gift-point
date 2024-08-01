package gift.Service;

import gift.ConverterToDto;
import gift.DTO.Category;
import gift.DTO.CategoryDto;
import gift.Repository.CategoryRepository;
import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

  private final CategoryRepository categoryRepository;

  public CategoryService(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  public List<CategoryDto> getAllCategories() {
    List<Category> categories = categoryRepository.findAll();
    List<CategoryDto> categoryDtos = categories.stream()
      .map(ConverterToDto::convertToCategoryDto)
      .toList();
    return categoryDtos;
  }

  public CategoryDto getCategoryById(Long id) {
    Category category = categoryRepository.findById(id)
      .orElseThrow(() -> new EmptyResultDataAccessException("해당 카테고리가 없습니다.", 1));
    return ConverterToDto.convertToCategoryDto(category);
  }

  public CategoryDto addCategory(CategoryDto categoryDto) {
    Category category = new Category(categoryDto.getId(), categoryDto.getName(),
      categoryDto.getColor(),
      categoryDto.getImageUrl(), categoryDto.getDescription());
    Category addedCategory = categoryRepository.save(category);
    return ConverterToDto.convertToCategoryDto(addedCategory);
  }

  public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
    Category category = categoryRepository.findById(id)
      .orElseThrow(() -> new EmptyResultDataAccessException("해당 카테고리가 없습니다.", 1));
    Category updateCategory = new Category(id, categoryDto.getName(),
      categoryDto.getColor(), categoryDto.getImageUrl(), categoryDto.getDescription());
    Category updatedCategory = categoryRepository.save(updateCategory);
    return ConverterToDto.convertToCategoryDto(updatedCategory);
  }

  public CategoryDto deleteCategory(Long id) {
    Category category = categoryRepository.findById(id)
      .orElseThrow(() -> new EmptyResultDataAccessException("해당 카테고리가 없습니다.", 1));
    categoryRepository.deleteById(id);
    return ConverterToDto.convertToCategoryDto(category);
  }
}
