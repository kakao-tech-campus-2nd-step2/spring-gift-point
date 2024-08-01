package gift.web.dto.product;

import gift.web.dto.OptionDto;
import java.util.List;

public record ProductResponseDto(
    Long id,
    String name,
    Long price,
    String imageUrl,
    Long categoryId,
    List<OptionDto> options
) {

}
