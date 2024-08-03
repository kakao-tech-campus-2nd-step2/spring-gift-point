package gift.product.application.dto;

import gift.option.persistence.Option;
import java.util.List;

public record ProductResponseDto(Long id, String name, int price, String imageUrl, Long categoryId, List<Option> options) {

}
