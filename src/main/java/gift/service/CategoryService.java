package gift.service;

import gift.domain.Category;
import gift.dto.requestdto.CategoryRequestDTO;
import gift.dto.responsedto.CategoryResponseDTO;
import gift.repository.JpaCategoryRepository;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CategoryService {
    private final JpaCategoryRepository jpaCategoryRepository;

    public CategoryService(JpaCategoryRepository jpaCategoryRepository) {
        this.jpaCategoryRepository = jpaCategoryRepository;
    }

    public Long addCategory(CategoryRequestDTO categoryRequestDTO){
        Category category = categoryRequestDTO.toEntity();
        return jpaCategoryRepository.save(category).getId();
    }

    @Transactional(readOnly = true)
    public CategoryResponseDTO getOneCategory(Long categoryId){
        Category category = getCategory(categoryId);
        return CategoryResponseDTO.from(category);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> getAllCategories(){
        return jpaCategoryRepository.findAll()
            .stream()
            .map(CategoryResponseDTO::from)
            .toList();
    }

    public Long updateCategory(Long categoryId, CategoryRequestDTO categoryRequestDTO) {
        Category category = getCategory(categoryId);

        category.update(categoryRequestDTO.name(), categoryRequestDTO.color(),
            categoryRequestDTO.imageUrl(),
            categoryRequestDTO.description());

        return category.getId();
    }

    public Long deleteCategory(Long categoryId) {
        Category category = getCategory(categoryId);
        jpaCategoryRepository.delete(category);
        return category.getId();
    }

    private Category getCategory(Long categoryId) {
        Category category = jpaCategoryRepository.findById(categoryId)
            .orElseThrow(() -> new NoSuchElementException("id가 잘못되었습니다."));
        return category;
    }
}
