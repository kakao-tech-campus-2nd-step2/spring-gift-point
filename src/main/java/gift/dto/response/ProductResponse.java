package gift.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import gift.dto.ProductDto;

public class ProductResponse {

    private ProductDto productDto;

    public ProductResponse(
        @JsonProperty("product")
        ProductDto productDto
    ){
        this.productDto = productDto;
    }

    public ProductDto getProductDto(){
        return productDto;
    }
    
}
