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
        categoryRepository.findByName(saveCategoryDTO.name()).ifPresent(c -> {
            throw new BadRequestException("이미 존재하는 카테고리");
        });
        Category category = new Category(saveCategoryDTO);
        return categoryRepository.save(category).toResponseDTO();
    }

    @Transactional
    public ResponseCategoryDTO updateCategory(int id, SaveCategoryDTO saveCategoryDTO) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("존재 하지 않는 카테고리입니다."));
        categoryRepository.findByName(saveCategoryDTO.name()).ifPresent(c -> {
            throw new BadRequestException("이미 존재하는 카테고리");
        });
        category = category.updateCategory(saveCategoryDTO);
        return category.toResponseDTO();
    }

    public ResponseCategoryDTO deleteCategory(int categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException("존재하지 않는 카테고리입니다."));
        category.checkEmpty();
        categoryRepository.delete(category);
        return category.toResponseDTO();
    }

    public List<ResponseCategoryDTO> getCategory() {
        return categoryRepository.findAll().stream().map(Category::toResponseDTO).toList();
    }
}
