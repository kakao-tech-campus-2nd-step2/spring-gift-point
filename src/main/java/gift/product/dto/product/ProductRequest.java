package gift.product.dto.product;

import gift.product.dto.option.OptionDto;
import java.util.List;

public interface ProductRequest {
    String name();
    int price();
    String imageUrl();
    Long categoryId();
    List<OptionDto> options();
}
