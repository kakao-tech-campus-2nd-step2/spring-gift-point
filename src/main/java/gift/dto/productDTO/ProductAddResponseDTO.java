package gift.dto.productDTO;

import gift.dto.optionDTO.OptionResponseDTO;
import java.util.List;

public record ProductAddResponseDTO(
    Long id,
    String name,
    String price,
    String imageUrl,
    Long categoryId,
    List<OptionResponseDTO> options
) {

}
