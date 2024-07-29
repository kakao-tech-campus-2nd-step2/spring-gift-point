package gift.service;

import gift.DTO.Category.CategoryRequest;
import gift.DTO.Category.CategoryResponse;
import gift.domain.Category;
import gift.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }
    /*
     * 카테고리를 저장하는 로직
     */
    @Transactional
    public CategoryResponse save(CategoryRequest categoryRequest){
        Category category = new Category(
                categoryRequest.getName()
        );

        Category savedCategory = categoryRepository.save(category);
        return new CategoryResponse(savedCategory);
    }
    /*
     * 카테고리를 조회하는 로직
     */
    public List<CategoryResponse> findAll(){
        List<CategoryResponse> answer = new ArrayList<>();

        List<Category> categories = categoryRepository.findAll();
        for (Category category : categories) {
            answer.add(new CategoryResponse(category));
        }

        return answer;
    }
    /*
     * 카테고리를 갱신하는 로직
     */
    @Transactional
    public void update(Long id, CategoryRequest categoryRequest){
        Category savedCategory = categoryRepository.findById(id).orElseThrow(NoSuchFieldError::new);

        savedCategory.update(categoryRequest.getName());
    }
    /*
     * 카테고리를 삭제하는 로직
     */
    @Transactional
    public void delete(Long id){
        categoryRepository.deleteById(id);
    }
}
