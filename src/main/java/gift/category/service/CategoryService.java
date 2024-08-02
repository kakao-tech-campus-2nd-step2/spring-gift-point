package gift.category.service;

import gift.category.dto.CategoryRequestDto;
import gift.category.dto.CategoryResponseDto;
import gift.category.entity.Category;
import gift.category.repository.CategoryRepository;
import gift.exception.BadRequestException;
import gift.exception.DuplicateResourceException;
import gift.exception.ResourceNotFoundException;
import gift.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

  private final CategoryRepository categoryRepository;

  private final ProductRepository productRepository;

  public CategoryService(CategoryRepository categoryRepository,
      ProductRepository productRepository) {
    this.categoryRepository = categoryRepository;
    this.productRepository = productRepository;
  }

  @Transactional
  public List<CategoryResponseDto> getAllCategories() {
    List<Category> categories = categoryRepository.findAll();

    if (categories.isEmpty()) {
      throw new ResourceNotFoundException("등록된 카테고리가 존재하지 않습니다.");
    }

    return categories.stream().map(CategoryResponseDto::toDto).collect(Collectors.toList());
  }

  @Transactional
  public CategoryResponseDto createCategory(@Valid CategoryRequestDto categoryRequestDto) {
    if (categoryRepository.existsByName(categoryRequestDto.name())) {
      throw new DuplicateResourceException("이미 존재하는 카테고리 이름입니다.");
    }

    Category category = new Category(categoryRequestDto.name(), categoryRequestDto.color(),
        categoryRequestDto.imageUrl(), categoryRequestDto.description());

    categoryRepository.save(category);
    return CategoryResponseDto.toDto(category);
  }

  @Transactional
  public CategoryResponseDto updateCategory(Long categoryId,
      @Valid CategoryRequestDto categoryRequestDto) {
    Category category = categoryRepository.findById(categoryId)
        .orElseThrow(() -> new ResourceNotFoundException("해당 카테고리를 찾을 수 없습니다."));

    if (categoryRepository.existsByName(categoryRequestDto.name())) {
      throw new DuplicateResourceException("이미 존재하는 카테고리 이름입니다.");
    }

    category.update(categoryRequestDto);
    return CategoryResponseDto.toDto(category);
  }

  @Transactional
  public CategoryResponseDto getCategory(Long categoryId) {
    Category category = categoryRepository.findById(categoryId)
        .orElseThrow(() -> new ResourceNotFoundException("해당 카테고리를 찾을 수 없습니다."));
    return CategoryResponseDto.toDto(category);
  }

  @Transactional
  public CategoryResponseDto deleteCategory(Long categoryId) {
    Category category = categoryRepository.findById(categoryId)
        .orElseThrow(() -> new ResourceNotFoundException("해당 카테고리를 찾을 수 없습니다."));

    categoryRepository.delete(category);
    return CategoryResponseDto.toDto(category);
  }
}
