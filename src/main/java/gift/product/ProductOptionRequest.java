package gift.product;

import gift.option.OptionRequest;

public class ProductOptionRequest {
    ProductRequest productRequest;
    OptionRequest optionRequest;

    public ProductOptionRequest() {
    }

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
