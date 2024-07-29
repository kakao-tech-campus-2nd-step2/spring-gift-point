package gift.service;

import gift.dto.CategoryDTO;
import gift.model.Category;
import gift.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private static final Logger log = LoggerFactory.getLogger(CategoryService.class);
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public void saveCategory(CategoryDTO categoryDTO){
        Optional<Category> existCategory = categoryRepository.findByName(categoryDTO.getName());
        if(existCategory.isPresent()){
            throw new IllegalArgumentException("이미 존재하는 카테고리 입니다.");
        }
        Category category = new Category(categoryDTO.getName());
        categoryRepository.save(category);
    }

    public List<CategoryDTO> getAllCategories(){
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDTO> categoryDTOList = categories.stream()
                .map(CategoryDTO::getCategoryDTO)
                .toList();
        return categoryDTOList;
    }
}
