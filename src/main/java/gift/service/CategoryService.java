package gift.service;

import gift.exception.ErrorCode;
import gift.exception.customException.CustomNotFoundException;
import gift.model.dto.CategoryDTO;
import gift.model.entity.Category;
import gift.repository.CategoryRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public CategoryDTO insertCategory(CategoryDTO categoryDTO) {
        return new CategoryDTO(categoryRepository.save(categoryDTO.toEntity()));
    }


    @Transactional(readOnly = true)
    public CategoryDTO getCategoryById(Long id) {
        Category saved = categoryRepository.findById(id)
            .orElseThrow(() -> new CustomNotFoundException(
                ErrorCode.CATEGORY_NOT_FOUND));
        return new CategoryDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<CategoryDTO> getCategoryList() {
        return categoryRepository.findAll().stream().map(CategoryDTO::new)
            .collect(Collectors.toList());
    }

    @Transactional
    public CategoryDTO updateCategory(CategoryDTO categoryDTO) {
        return new CategoryDTO(categoryRepository.save(categoryDTO.toEntity()));
    }

    @Transactional(readOnly = true)
    public boolean isDuplicateName(String name) {
        return categoryRepository.existsByName(name);
    }
}
