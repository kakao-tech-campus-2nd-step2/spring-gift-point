package gift.product;

import gift.option.Option;
import java.util.List;

public record ProductResponseDto(Long id, String name, int price, String imageUrl, Long categoryId, List<Option> options) {

}

