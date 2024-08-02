package gift.service;

import gift.dto.category.ResponseCategoryDTO;
import gift.dto.category.SaveCategoryDTO;
import gift.entity.Category;
import gift.exception.exception.BadRequestException;
import gift.exception.exception.NotFoundException;
import gift.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public ResponseCategoryDTO saveCategory(SaveCategoryDTO saveCategoryDTO) {
        checkDuplicateByName(saveCategoryDTO.name());
        Category category = new Category(saveCategoryDTO);
        return categoryRepository.save(category).toResponseDTO();
    }

    @Transactional
    public ResponseCategoryDTO updateCategory(int id, SaveCategoryDTO saveCategoryDTO) {
        Category category = findCategoryById(id);
        checkDuplicateByName(saveCategoryDTO.name());
        category = category.updateCategory(saveCategoryDTO);
        return category.toResponseDTO();
    }

    public ResponseCategoryDTO deleteCategory(int categoryId) {
        Category category = findCategoryById(categoryId);
        category.checkEmpty();
        categoryRepository.delete(category);
        return category.toResponseDTO();
    }

    public List<ResponseCategoryDTO> getCategory() {
        return categoryRepository.findAll().stream().map(Category::toResponseDTO).toList();
    }

    private Category findCategoryById(int categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException("존재하지 않는 카테고리입니다."));
    }

    private void checkDuplicateByName(String name) {
        categoryRepository.findByName(name).ifPresent(c -> {
            throw new BadRequestException("이미 존재하는 카테고리");
        });
    }
}
