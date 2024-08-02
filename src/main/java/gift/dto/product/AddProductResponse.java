package gift.dto.product;

import gift.dto.OptionDTO;
import java.util.List;

public record AddProductResponse(
    ProductDto productDto,
    List<OptionDTO> optionDTOs
) {

}
