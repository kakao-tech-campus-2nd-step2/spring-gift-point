package gift.Service;

import gift.Exception.Category.CategoryDuplicatedException;
import gift.Exception.Category.CategoryNotFoundException;
import gift.Model.request.CategoryRequest;
import gift.Model.response.CategoryResponse;
import gift.Model.Entity.CategoryEntity;
import gift.Repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }

    public void create(CategoryRequest categoryRequest){
        Optional<CategoryEntity> categoryEntityOptional = categoryRepository.findByName(categoryRequest.name());

        if(categoryEntityOptional.isPresent()){
            throw new CategoryDuplicatedException("중복된 카테고리가 이미 있습니다.");
        }

        categoryRepository.save(new CategoryEntity(categoryRequest.name(), categoryRequest.imageUrl(), categoryRequest.description()));
    }

    public List<CategoryResponse> read(){
        List<CategoryEntity> categoryEntityList = categoryRepository.findAll();

        List<CategoryResponse> categoryResponseList = new ArrayList<>();

        for(CategoryEntity c: categoryEntityList){
            categoryResponseList.add(c.mapToDTO());
        }

        return categoryResponseList;
    }

    public void update(Long categoryId, CategoryRequest categoryRequest){
        Optional<CategoryEntity> categoryEntityOptional = categoryRepository.findById(categoryId);

        if(categoryEntityOptional.isEmpty()){
            throw new CategoryNotFoundException("카테고리를 찾을 수 없습니다.");
        }
        CategoryEntity categoryEntity = new CategoryEntity(categoryRequest.name(), categoryRequest.imageUrl(), categoryRequest.description());
        categoryEntity.setId(categoryId);
        categoryRepository.save(categoryEntity);
    }

    public void delete(Long id){
        Optional<CategoryEntity> categoryEntityOptional = categoryRepository.findById(id);

        if(categoryEntityOptional.isEmpty()){
            throw new CategoryNotFoundException("카테고리를 찾을 수 없습니다.");
        }

        categoryRepository.deleteById(id);
    }
}
