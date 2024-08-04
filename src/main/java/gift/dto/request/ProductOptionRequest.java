package gift.dto.request;

import jakarta.validation.Valid;

public class ProductOptionRequest {
    @Valid
    private ProductRequest productRequest;

    @Valid
    private OptionRequest optionRequest;

    public ProductRequest getProductRequest() {
        return productRequest;
    }

    public OptionRequest getOptionRequest() {
        return optionRequest;
    }
}
