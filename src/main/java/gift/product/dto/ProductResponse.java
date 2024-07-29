package gift.product.dto;

import java.util.Set;

public record ProductResponse(Long id,
                              String name,
                              int price,
                              String imageUrl,
                              String categoryName,
                              Set<OptionResponse> options) { }