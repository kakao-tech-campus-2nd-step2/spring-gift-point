package gift.dto;

import java.util.List;

public record ProductGetResponseDTO(
    Long id,
    String name,
    String price,
    String imageUrl,
    Long categoryId,
    List<OptionResponseDTO> options
) {

}
