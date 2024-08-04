package gift.product.dto;

import java.util.List;

public record AddProductResponse(
    ProductDto productDto,
    List<OptionDto> optionDtos
) {

}
