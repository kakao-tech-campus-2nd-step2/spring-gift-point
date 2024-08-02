package gift.dto.productDTO;

import gift.dto.optionDTO.OptionRequestDTO;
import java.util.List;

public record ProductAddRequestDTO(
    String name,
    String price,
    String imageUrl,
    Long categoryId,
    List<OptionRequestDTO> options
) {

}