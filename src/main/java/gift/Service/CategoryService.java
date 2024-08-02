package gift.Service;

import gift.DTO.CategoryDTO;
import gift.Entity.CategoryEntity;
import gift.Repository.CategoryRepository;
import gift.Mapper.CategoryServiceMapper;
import org.springframework.stereotype.Service;


@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryServiceMapper categoryServiceMapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryServiceMapper categoryServiceMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryServiceMapper = categoryServiceMapper;
    }

    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        CategoryEntity categoryEntity = categoryServiceMapper.convertToEntity(categoryDTO);
        categoryRepository.save(categoryEntity);
        return categoryServiceMapper.convertToDTO(categoryEntity);
    }

    public CategoryDTO findById(Long id) {
        CategoryEntity category = categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("잘못된 접근입니다"));
        return categoryServiceMapper.convertToDTO(category);
    }

    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        CategoryEntity existingCategory = categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("잘못된 접근입니다"));
        existingCategory.setName(categoryDTO.getName());
        // 카테고리는 이름만 바뀐다고 가정.
        categoryRepository.save(existingCategory);
        return categoryServiceMapper.convertToDTO(existingCategory);
    }

    // cascade 설정으로 인한 deleteCategory 기능은 필요 없어짐
}
