package gift.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import gift.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import gift.dto.CategoryDto;
import gift.dto.response.CategoryResponse;
import gift.entity.Category;
import gift.exception.CustomException;

import java.util.List;

@Service
public class CategoryService {
    
    private CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository){

        this.categoryRepository = categoryRepository;
        Category testCategory = new Category("test", "color", "imageUrl", "");
        Category testCategory2 = new Category("test2", "color", "imageUrl", "");

        categoryRepository.save(testCategory);
        categoryRepository.save(testCategory2);
    }

    public CategoryResponse findAll(){

        List<Category> categories = categoryRepository.findAll();
        CategoryResponse categoryResponse = new CategoryResponse(
            categories
            .stream()
            .map(CategoryDto::fromEntity)
            .toList());

        return categoryResponse;
    }

    @Transactional
    public void addCategory(CategoryDto categoryDto){

        if(categoryRepository.findByName(categoryDto.getName()).isEmpty()){
            Category category = toEntity(categoryDto);
            categoryRepository.save(category);
        }else{
            new CustomException("Category with name" + categoryDto.getName() + "exists" , HttpStatus.CONFLICT);
        }
    }

    public Category toEntity(CategoryDto categoryDto){
        return new Category(categoryDto.getName(), categoryDto.getColor(), categoryDto.getImageUrl(), categoryDto.getDescription());
    }

    
}
