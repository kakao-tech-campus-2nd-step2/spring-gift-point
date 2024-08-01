package gift.dto;

import java.util.List;

public record ProductAddRequestDTO(
    String name,
    String price,
    String imageUrl,
    Long categoryId,
    List<OptionRequestDTO> options
) {

}
