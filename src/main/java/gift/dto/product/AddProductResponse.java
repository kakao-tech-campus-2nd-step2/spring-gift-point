package gift.dto.product;

import gift.dto.option.OptionDto;
import java.util.List;

public record AddProductResponse(
    ProductDto productDto,
    List<OptionDto> optionDtos
) {

}
