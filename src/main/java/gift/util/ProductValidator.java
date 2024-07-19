package gift.util;

import gift.dto.OptionDTO;
import gift.dto.ProductRequest;

public class ProductValidator {
    public static void validateProductRequest(ProductRequest productRequest) {
        if(productRequest.getOptions() == null || productRequest.getOptions().isEmpty()) {
            throw new IllegalArgumentException("Product must have at least one option.");
        }
    }
}
