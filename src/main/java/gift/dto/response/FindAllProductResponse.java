package gift.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import gift.dto.CategoryDto;
import gift.dto.ProductInfo;

import java.util.List;


public class FindAllProductResponse {
    
    private CategoryDto categoryDto;
    private List<ProductInfo> productInfos;

    @JsonCreator
    public FindAllProductResponse(
        @JsonProperty("category")
        CategoryDto categoryDto,

        @JsonProperty("products")
        List<ProductInfo> productInfos
    ){
        this.categoryDto = categoryDto;
        this.productInfos = productInfos;
    }

    public CategoryDto getCategoryDto(){
        return categoryDto;
    }

    public List<ProductInfo> geProductInfos(){
        return productInfos;
    }

}
