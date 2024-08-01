package gift.category.service;

import gift.category.dto.CategoryRequestDto;
import gift.category.dto.CategoryResponseDto;
import gift.category.entity.Category;
import gift.category.repository.CategoryRepository;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // 카테고리를 추가하는 핸들러.
    public void insertCategory(CategoryRequestDto categoryRequestDto) {
        var category = new Category(categoryRequestDto.name(), categoryRequestDto.imageUrl());

        categoryRepository.save(category);
    }

    // 하나만 조회하는 핸들러
    @Transactional
    public CategoryResponseDto selectCategory(Long categoryId) {
        var actualCategory = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new NoSuchElementException("존재하지 않는 카테고리입니다."));

        return CategoryResponseDto.fromCategory(actualCategory);
    }

    // 카테고리를 조회하는 핸들러.
    @Transactional(readOnly = true)
    public List<CategoryResponseDto> selectCategories() {
        var categories = categoryRepository.findAll();

        return categories.stream().map(CategoryResponseDto::fromCategory).toList();
    }

    // 카테고리를 수정하는 핸들러.
    @Transactional
    public void updateCategory(long categoryId, CategoryRequestDto categoryRequestDto) {
        var actualCategory = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new NoSuchElementException("존재하지 않는 카테고리입니다."));

        actualCategory.updateCategory(categoryRequestDto.name(), categoryRequestDto.imageUrl());
    }

    // 카테고리를 삭제하는 핸들러.
    @Transactional
    public void deleteCategory(long categoryId) {
        verifyCategoryExists(categoryId);

        categoryRepository.deleteById(categoryId);
    }

    // id로 카테고리가 존재하는지 검증. delete에서 사용
    private void verifyCategoryExists(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new NoSuchElementException("존재하지 않는 카테고리입니다.");
        }
    }
}
