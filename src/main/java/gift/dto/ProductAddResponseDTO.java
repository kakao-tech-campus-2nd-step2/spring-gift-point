package gift.dto;

import java.util.List;

public record ProductAddResponseDTO(
    Long id,
    String name,
    String price,
    String imageUrl,
    Long categoryId,
    List<OptionResponseDTO>options
) {

}
