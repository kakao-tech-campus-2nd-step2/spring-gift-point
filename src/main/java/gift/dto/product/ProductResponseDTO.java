package gift.dto.product;

import gift.model.Option;

import java.util.List;

public record ProductResponseDTO(Long id,
                                 String name,
                                 Long price,
                                 String imageUrl,
                                 Long categoryId,
                                 List<Option> options) { }
