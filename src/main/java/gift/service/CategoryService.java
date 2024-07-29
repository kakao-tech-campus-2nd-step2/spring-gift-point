package gift.service;

import gift.dto.category.CategoryDTO;
import gift.entity.Category;
import gift.exception.exception.BadRequestException;
import gift.exception.exception.NotFoundException;
import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryDTO saveCategory(CategoryDTO categoryDTO) {
        categoryRepository.findByName(categoryDTO.name()).ifPresent(c -> {
            throw new BadRequestException("이미 존재하는 카테고리");
        });
        Category category = new Category(categoryDTO);
        return categoryRepository.save(category).toDTO();
    }

    @Transactional
    public CategoryDTO updateCategory(CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(categoryDTO.id()).orElseThrow(() -> new NotFoundException("존재하지 않는 카테고리입니다."));
        categoryRepository.findByName(categoryDTO.name()).ifPresent(c -> {
            throw new BadRequestException("이미 존재하는 카테고리");
        });
        category.updateCategoryName(categoryDTO.name());
        return category.toDTO();
    }

    public void deleteCategory(int categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException("존재하지 않는 카테고리입니다."));
        category.checkEmpty();
        categoryRepository.delete(category);
    }

    public Page<CategoryDTO> getCategory(Pageable pageable) {
        return categoryRepository.findAllCategory(pageable);
    }
}
