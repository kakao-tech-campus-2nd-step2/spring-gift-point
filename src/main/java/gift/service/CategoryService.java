package gift.service;

import gift.domain.Category;
import gift.dto.request.CategoryRequest;
import gift.dto.response.CategoryResponse;
import gift.exception.customException.CategoryNotFoundException;
import gift.exception.customException.DuplicateCategoryNameException;
import gift.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static gift.exception.errorMessage.Messages.CATEGORY_NAME_ALREADY_EXISTS;
import static gift.exception.errorMessage.Messages.NOT_FOUND_CATEGORY;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public void save(CategoryRequest categoryDto){
        if(categoryRepository.existsByName(categoryDto.name())){
            throw new DuplicateCategoryNameException(CATEGORY_NAME_ALREADY_EXISTS);
        }
        categoryRepository.save(categoryDto.toEntity());
    }

    @Transactional(readOnly = true)
    public CategoryResponse findById(Long id){
        return categoryRepository.findById(id)
                .map(CategoryResponse::from)
                .orElseThrow(()->new CategoryNotFoundException(NOT_FOUND_CATEGORY));
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> findAll(){
        return categoryRepository.findAll()
                .stream()
                .map(CategoryResponse::from)
                .toList();
    }

    @Transactional
    public void deleteById(Long id){
        categoryRepository.findById(id)
                        .orElseThrow(() -> new CategoryNotFoundException(NOT_FOUND_CATEGORY));
        categoryRepository.deleteById(id);
    }

    @Transactional
    public void updateById(Long id, CategoryRequest request){
        Category foundCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(NOT_FOUND_CATEGORY));

        foundCategory.updateCategory(request.name(), request.color(), request.imageUrl(), request.description());
    }

}
