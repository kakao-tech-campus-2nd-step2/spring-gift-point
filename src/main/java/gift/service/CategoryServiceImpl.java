package gift.service;


import gift.dto.Request.CategoryRequestDto;
import gift.dto.Response.CategoryResponseDto;
import gift.model.Category;
import gift.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoryResponseDto> getAllCategories() {
        return categoryRepository.findAll().stream()
            .map(this::convertToResponseDto)
            .collect(Collectors.toList());
    }

    @Override
    public CategoryResponseDto getCategoryById(Long id) {
        return categoryRepository.findById(Math.toIntExact(id))
            .map(this::convertToResponseDto)
            .orElse(null);
    }

    @Override
    public void saveCategory(CategoryRequestDto categoryRequestDto) {
        Category category = convertToEntity(categoryRequestDto);
        categoryRepository.save(category);
    }

    @Override
    public void updateCategory(Long id, CategoryRequestDto categoryRequestDto) {
        Category category = categoryRepository.findById(Math.toIntExact(id)).orElseThrow();
        updateEntityFromDto(category, categoryRequestDto);
        categoryRepository.save(category);
    }

    private CategoryResponseDto convertToResponseDto(Category category) {
        return new CategoryResponseDto(
            category.getId(),
            category.getName(),
            category.getColor(),
            category.getImageUrl(),
            category.getDescription()
        );
    }

    private Category convertToEntity(CategoryRequestDto categoryRequestDto) {
        Category category = new Category();
        category.setName(categoryRequestDto.name());
        category.setColor(categoryRequestDto.color());
        category.setImageUrl(categoryRequestDto.imageUrl());
        category.setDescription(categoryRequestDto.description());
        return category;
    }

    private void updateEntityFromDto(Category category, CategoryRequestDto categoryRequestDto) {
        category.setName(categoryRequestDto.name());
        category.setColor(categoryRequestDto.color());
        category.setImageUrl(categoryRequestDto.imageUrl());
        category.setDescription(categoryRequestDto.description());
    }
}
