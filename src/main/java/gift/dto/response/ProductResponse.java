package gift.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import gift.dto.ProductDto;

public class ProductResponse {

    private ProductDto product;

    public ProductResponse(
        @JsonProperty("product")
        ProductDto product
    ){
        this.product = product;
    }

    public ProductDto getProduct(){
        return product;
    }
    
}
