package gift.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import gift.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import gift.dto.CategoryDto;
import gift.dto.request.CategoryRequest;
import gift.dto.response.CategoryResponse;
import gift.dto.response.GetCategoriesResponse;
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

    public GetCategoriesResponse findAll(){

        List<Category> categories = categoryRepository.findAll();
        GetCategoriesResponse categoryResponse = new GetCategoriesResponse(
            categories
            .stream()
            .map(CategoryDto::fromEntity)
            .toList());

        return categoryResponse;
    }

    public CategoryDto findById(Long categoryId){
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new CustomException("Category with id " + categoryId + " not exists", HttpStatus.NOT_FOUND, -40402));
        return new CategoryDto(categoryId, category.getName(), category.getColor(), category.getImageUrl(), category.getDescription());
    }

    @Transactional
    public CategoryResponse addCategory(CategoryRequest categoryRequest){

        if(categoryRepository.findByName(categoryRequest.getName()).isEmpty()){
            Category category = new Category(categoryRequest.getName(), categoryRequest.getColor(), categoryRequest.getImageUrl(), categoryRequest.getDescription());
            Category savedCategory = categoryRepository.save(category);
            return new CategoryResponse(new CategoryDto(
                savedCategory.getId(), 
                savedCategory.getName(), 
                savedCategory.getColor(), 
                savedCategory.getImageUrl(), 
                savedCategory.getDescription()));
        }else{
            throw new CustomException("Category with name" + categoryRequest.getName() + "exists" , HttpStatus.CONFLICT, -40902);
        }
    }

    @Transactional
    public CategoryResponse updateCategory(Long categoryId, CategoryRequest categoryRequest){

        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new CustomException("Category with id " + categoryId + " not exists", HttpStatus.NOT_FOUND, -40402));
        
        return new CategoryResponse(category.update(categoryRequest));
    }

    public Category toEntity(CategoryDto categoryDto){
        return new Category(categoryDto.getName(), categoryDto.getColor(), categoryDto.getImageUrl(), categoryDto.getDescription());
    }
    
}
