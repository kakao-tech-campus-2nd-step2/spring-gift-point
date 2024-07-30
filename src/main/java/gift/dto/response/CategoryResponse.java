package gift.dto.response;

import java.util.List;

import gift.dto.CategoryDto;

public class CategoryResponse {
    
    private List<CategoryDto> categories;

    public CategoryResponse(List<CategoryDto> categories){
        this.categories = categories;
    }

    public List<CategoryDto> getCategories(){
        return this.categories;
    }

}
