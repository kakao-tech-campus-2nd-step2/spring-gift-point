package gift.administrator.category;

import gift.error.NotFoundIdException;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
            .map(CategoryDTO::fromCategory)
            .toList();
    }

    public CategoryDTO getCategoryById(long id) {
        Category category = findByCategoryId(id);
        return CategoryDTO.fromCategory(category);
    }

    public CategoryDTO addCategory(CategoryDTO categoryDTO) {
        if (existsByName(categoryDTO.getName())) {
            throw new IllegalArgumentException("존재하는 이름입니다.");
        }
        return CategoryDTO.fromCategory(categoryRepository.save(categoryDTO.toCategory()));
    }

    private Category findByCategoryId(long categoryId){
        return categoryRepository.findById(categoryId)
            .orElseThrow(() -> new NotFoundIdException("카테고리 아이디를 찾을 수 없습니다."));
    }

    public CategoryDTO updateCategory(CategoryDTO categoryDTO, long categoryId) {
        Category category = findByCategoryId(categoryId);
        if (categoryRepository.existsByNameAndIdNot(categoryDTO.getName(), categoryId)) {
            throw new IllegalArgumentException("존재하는 이름입니다.");
        }
        category.update(categoryDTO.getName(), categoryDTO.getColor(), categoryDTO.getImageUrl(),
            categoryDTO.getDescription());
        return CategoryDTO.fromCategory(categoryRepository.save(category));
    }

    public void deleteCategory(long id) {
        if(!categoryRepository.existsById(id)){
            throw new NotFoundIdException("삭제하려는 카테고리가 존재하지 않습니다.");
        }
        categoryRepository.deleteById(id);
    }

    private boolean existsByName(String name) {
        return categoryRepository.existsByName(name);
    }

    public void existsByNameThrowException(String name){
        if(existsByName(name)){
            throw new IllegalArgumentException("존재하는 이름입니다.");
        }
    }

    public void existsByNameAndId(String name, long id) {
        if (existsByName(name) && !Objects.equals(getCategoryById(id).getName(), name)) {
            throw new IllegalArgumentException("존재하는 이름입니다.");
        }
    }
}
