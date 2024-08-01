package gift.dto.product;

import gift.dto.option.OptionDTO;

public record ProductAddResponseDTO(Long id,
                                    String name,
                                    Long price,
                                    String imageUrl,
                                    Long categoryId,
                                    OptionDTO option ) {
}
