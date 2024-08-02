package gift.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import gift.dto.CategoryDto;
import gift.dto.ProductInfo;

import java.util.List;


public class GetProductsResponse {
    
    private CategoryDto category;
    private List<ProductInfo> products;

    @JsonCreator
    public GetProductsResponse(
        @JsonProperty("category")
        CategoryDto category,

        @JsonProperty("products")
        List<ProductInfo> products
    ){
        this.category = category;
        this.products = products;
    }

    public CategoryDto getCategory(){
        return category;
    }

    public List<ProductInfo> geProducts(){
        return products;
    }

}
