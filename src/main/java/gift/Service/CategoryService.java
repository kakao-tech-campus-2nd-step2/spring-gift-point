package gift.Service;

import gift.DTO.CategoryResponseDTO;
import gift.Entity.CategoryEntity;
import gift.Repository.CategoryRepository;
//import gift.Mapper.CategoryServiceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
//    private final CategoryServiceMapper categoryServiceMapper;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

//    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
//        CategoryEntity categoryEntity = categoryServiceMapper.convertToEntity(categoryDTO);
//        categoryRepository.save(categoryEntity);
//        return categoryServiceMapper.convertToDTO(categoryEntity);
//    }
//
//    public CategoryDTO findById(Long id) {
//        CategoryEntity category = categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("잘못된 접근입니다"));
//        return categoryServiceMapper.convertToDTO(category);
//    }

//    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
//        CategoryEntity existingCategory = categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("잘못된 접근입니다"));
//        existingCategory.setName(categoryDTO.getName());
//        // 카테고리는 이름만 바뀐다고 가정.
//        categoryRepository.save(existingCategory);
//        return categoryServiceMapper.convertToDTO(existingCategory);
//    }


    public List<CategoryEntity> getAllCategories() {
        return categoryRepository.findAll();
    }


}
