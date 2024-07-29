package gift.response;

import gift.model.Product;
import java.util.List;

public record ProductOptionsResponse(ProductResponse product, List<OptionResponse> options) {

}
