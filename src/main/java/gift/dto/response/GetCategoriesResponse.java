package gift.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import gift.dto.CategoryDto;

public class GetCategoriesResponse {
    
    private List<CategoryDto> categories;

    @JsonCreator
    public GetCategoriesResponse(
        @JsonProperty("categories")
        List<CategoryDto> categories){
        this.categories = categories;
    }

    public List<CategoryDto> getCategories(){
        return this.categories;
    }

}
