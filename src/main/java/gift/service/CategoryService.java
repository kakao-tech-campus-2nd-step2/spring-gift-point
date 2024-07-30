package gift.service;

import gift.dto.CategoryRequestDTO;
import gift.dto.CategoryResponseDTO;
import gift.model.Category;
import gift.repository.CategoryRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // 모든 카테고리 조회
    public List<CategoryResponseDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
            .map(CategoryResponseDTO::fromEntity)
            .collect(Collectors.toList());
    }

    // 특정 ID의 카테고리 조회
    public CategoryResponseDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 카테고리를 찾을 수 없습니다."));
        return CategoryResponseDTO.fromEntity(category);
    }

    // 카테고리 생성
    public CategoryResponseDTO createCategory(CategoryRequestDTO categoryRequestDTO) {
        Category category = categoryRequestDTO.toEntity();
        category = categoryRepository.save(category);
        return CategoryResponseDTO.fromEntity(category);
    }

    // 카테고리 업데이트
    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO categoryRequestDTO) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 카테고리를 찾을 수 없습니다."));
        category.setName(categoryRequestDTO.getName());
        category = categoryRepository.save(category);
        return CategoryResponseDTO.fromEntity(category);
    }

    // 카테고리 삭제
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    // 카테고리 엔티티 조회
    public Category findCategoryEntityById(Long id) {
        return categoryRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 카테고리를 찾을 수 없습니다."));
    }
}
