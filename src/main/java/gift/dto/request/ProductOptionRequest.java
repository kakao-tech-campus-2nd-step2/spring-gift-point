package gift.dto.request;

import jakarta.validation.Valid;

public class ProductOptionRequest {
    @Valid
    private ProductRequest productRequest;

    @Valid
    private OptionRequest optionRequest;

    public ProductOptionRequest(ProductRequest productRequest, OptionRequest optionRequest) {
        this.productRequest = productRequest;
        this.optionRequest = optionRequest;
    }

    public ProductRequest getProductRequest() {
        return productRequest;
    }

    public void setProductRequest(ProductRequest productRequest) {
        this.productRequest = productRequest;
    }

    public OptionRequest getOptionRequest() {
        return optionRequest;
    }

    public void setOptionRequest(OptionRequest optionRequest) {
        this.optionRequest = optionRequest;
    }
}
