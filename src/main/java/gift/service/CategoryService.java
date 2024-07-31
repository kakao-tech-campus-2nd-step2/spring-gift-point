package gift.service;



import gift.dto.Request.CategoryRequestDto;
import gift.dto.Response.CategoryResponseDto;
import java.util.List;

public interface CategoryService {
    List<CategoryResponseDto> getAllCategories();
    CategoryResponseDto getCategoryById(Long id);
    void saveCategory(CategoryRequestDto categoryRequestDto);
    void updateCategory(Long id, CategoryRequestDto categoryRequestDto);
}
