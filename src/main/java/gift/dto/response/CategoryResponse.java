package gift.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import gift.dto.CategoryDto;

public class CategoryResponse {

    private CategoryDto categoryDto;
    
    @JsonCreator
    public CategoryResponse(
        @JsonProperty("created_category")
        CategoryDto categoryDto
    ){
        this.categoryDto = categoryDto;
    }

    public CategoryDto getCategoryDto(){
        return categoryDto;
    }
}
