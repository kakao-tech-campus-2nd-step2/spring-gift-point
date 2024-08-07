package gift.util;

<<<<<<< HEAD
import gift.dto.option.OptionRequest;
import gift.dto.product.ProductRequest;
=======
import gift.dto.OptionRequest;
import gift.dto.ProductRequest;
>>>>>>> e3b9ef38d18104514aa1d0951ff1a098ff9a093f

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
