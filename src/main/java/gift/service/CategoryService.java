package gift.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import gift.dto.CategoryRequest;
import gift.dto.CategoryResponse;
import gift.entity.Category;
import gift.exception.DuplicateCategoryNameException;
import gift.exception.InvalidCategoryException;
import gift.repository.CategoryRepository;

@Service
public class CategoryService {
	
	private CategoryRepository categoryRepository;
	
	public CategoryService(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}
	
	public List<CategoryResponse> getAllCategories(){
		return categoryRepository.findAll().stream()
				.map(Category::toDto)
				.collect(Collectors.toList());
	}
	
	public void createCategory(CategoryRequest request, BindingResult bindingResult) {
		validateBindingResult(bindingResult);
		Category category = request.toEntity();
		validateDuplicateCategoryName(category.getName());
		categoryRepository.save(category);
	}
	
	public void updateCategory(Long categoryId, CategoryRequest request, BindingResult bindingResult) {
		validateBindingResult(bindingResult);
		Category updatedCategory = request.toEntity();
		updatedCategory.setId(categoryId);
		validateCategoryId(categoryId);
		categoryRepository.save(updatedCategory);
	}
	
	private void validateBindingResult(BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			String errorMessage = bindingResult
					.getFieldError()
					.getDefaultMessage();
			throw new InvalidCategoryException(errorMessage);
		}
	}
	
	private void validateCategoryId(Long categoryId) {
		if (!categoryRepository.existsById(categoryId)) {
			throw new InvalidCategoryException("Category not foudn.");
		}
	}
	
	private void validateDuplicateCategoryName(String categoryName) {
		if (categoryRepository.existsByName(categoryName)) {
			throw new DuplicateCategoryNameException("This is the extracted Name.");
		}
	}
	
	public Category getCategoryById(Long categoryId) {
	    return categoryRepository.findById(categoryId)
	            .orElseThrow(() -> new InvalidCategoryException("Category not foudn."));
	}
}
