package gift.util;

import gift.dto.option.OptionRequest;
import gift.dto.product.ProductRequest;

public class ProductValidator {
    public static void validateProductRequest(ProductRequest productRequest) {
        if(productRequest.getOptions() == null || productRequest.getOptions().isEmpty()) {
            throw new IllegalArgumentException("Product must have at least one option.");
        }

        for(OptionRequest optionRequest : productRequest.getOptions()) {
            if(optionRequest.getQuantity() < 1 || optionRequest.getQuantity() >= 100_000_000) {
                throw new IllegalArgumentException("Quantity must be between 1 and 99,999,999");
            }
        }
    }
}
