package gift.service;

import gift.dto.request.CategoryRequestDTO;
import gift.dto.response.CategoryResponseDTO;
import gift.entity.Product;
import gift.entity.Category;
import gift.exception.categoryException.CategoryNotFoundException;
import gift.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import jdk.jfr.Description;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Description("category get method")
    public List<CategoryResponseDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Description("category add method")
    public void addCategory(CategoryRequestDTO categoryRequestDTO) {
        Category category = toEntity(categoryRequestDTO);
        categoryRepository.save(category);
    }

    @Description("category update method")
    public void updateCategory(Long categoryId, CategoryRequestDTO categoryRequestDTO){
        Category category = getCategoryEntity(categoryId);
        category.updateCategory(categoryRequestDTO);
        categoryRepository.save(category);
    }

    private Category getCategoryEntity(Long categoryId){
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
    }

    private CategoryResponseDTO toDTO(Category category) {
        List<String> productNames = category.getProducts()
                .stream()
                .map(Product::getName)
                .collect(Collectors.toList());

        return new CategoryResponseDTO(
                category.getId(),
                category.getName(),
                category.getColor(),
                category.getImageUrl(),
                category.getDescription()
        );
    }

    private Category toEntity(CategoryRequestDTO categoryRequestDTO) {
        Category category = new Category(
                categoryRequestDTO.name(),
                categoryRequestDTO.color(),
                categoryRequestDTO.imageUrl(),
                categoryRequestDTO.description()
        );
        return category;
    }

}

/*
Description("category remove method")
public void removeCategory(Long categoryId){
    System.out.println("remove category");
    Category category = getCategoryEntity(categoryId);
    categoryRepository.delete(category);
    //categoryRepository.deleteById(categoryId);
}


@Description("category get method")
public CategoryResponseDTO getCategory(Long categoryId){
    Category category = getCategoryEntity(categoryId);
    return toDTO(category);
}

*/