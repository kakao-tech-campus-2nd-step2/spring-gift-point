package gift.web.dto.product;

import gift.web.dto.option.OptionResponseDto;
import java.util.List;

public record ProductResponseDto(
    Long id,
    String name,
    Long price,
    String imageUrl,
    Long categoryId,
    List<OptionResponseDto> options
) {

}
