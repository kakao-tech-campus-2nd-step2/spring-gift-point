package gift.service;

import gift.dto.CategoryDto;
import gift.model.Category;
import gift.repository.CategoryRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category addCategory(CategoryDto categoryDto) {
        categoryRepository.findByName(categoryDto.getName())
                .ifPresent(existingCategory -> {
                    throw new DuplicateKeyException("이미 존재하는 카테고리 이름입니다.");
                });
        Category category = new Category(
                categoryDto.getName(),
                categoryDto.getColor(),
                categoryDto.getImageUrl(),
                categoryDto.getDescription()
        );
        categoryRepository.save(category);
        return category;
    }

    public Category updateCategory(Long id, CategoryDto categoryDto) {
        categoryRepository.findByName(categoryDto.getName())
                .ifPresent(existingCategory -> {
                    throw new DuplicateKeyException("이미 존재하는 카테고리 이름입니다.");
                });
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 카테고리입니다."));
        Category updatedCategory = new Category(
                existingCategory.getId(),
                categoryDto.getName(),
                categoryDto.getColor(),
                categoryDto.getImageUrl(),
                categoryDto.getDescription()
        );
        categoryRepository.save(updatedCategory);
        return existingCategory;
    }
}
