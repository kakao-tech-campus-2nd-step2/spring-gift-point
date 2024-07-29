package gift.Service;

import gift.Exception.Category.CategoryDuplicatedException;
import gift.Exception.Category.CategoryNotFoundException;
import gift.Model.DTO.CategoryDTO;
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

    public void create(CategoryDTO categoryDTO){
        Optional<CategoryEntity> categoryEntityOptional = categoryRepository.findByName(categoryDTO.name());

        if(categoryEntityOptional.isPresent()){
            throw new CategoryDuplicatedException("중복된 카테고리가 이미 있습니다.");
        }

        categoryRepository.save(new CategoryEntity(categoryDTO.name(), categoryDTO.color(), categoryDTO.imageUrl(), categoryDTO.description()));
    }

    public List<CategoryDTO> read(){
        List<CategoryEntity> categoryEntityList = categoryRepository.findAll();

        List<CategoryDTO> categoryDTOList = new ArrayList<>();

        for(CategoryEntity c: categoryEntityList){
            categoryDTOList.add(new CategoryDTO(c.getId(), c.getName(), c.getColor(), c.getImageUrl(), c.getDescription()));
        }

        return categoryDTOList;
    }

    public void update(CategoryDTO categoryDTO){
        Optional<CategoryEntity> categoryEntityOptional = categoryRepository.findByName(categoryDTO.name());

        if(categoryEntityOptional.isEmpty()){
            throw new CategoryNotFoundException("카테고리를 찾을 수 없습니다.");
        }
        CategoryEntity categoryEntity = new CategoryEntity(categoryDTO.name(), categoryDTO.color(), categoryDTO.imageUrl(), categoryDTO.description());
        categoryEntity.setId(categoryEntityOptional.get().getId());
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
