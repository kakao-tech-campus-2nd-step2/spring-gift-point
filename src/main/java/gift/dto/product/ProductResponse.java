package gift.dto.product;

import gift.dto.option.OptionResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class ProductResponse {
    @Setter
    private Long id;
    @Setter
    private String name;
    @Setter
    private Integer price;
    @Setter
    private String imageUrl;
    @Setter
    @NotNull(message = "category ID cannot be null")
    private Long categoryId;
    @NotEmpty(message = "Option list cannot be empty")
    private final List<OptionResponse> options;

    public ProductResponse(Long id, Long categoryId, String name, Integer price, String imageUrl , List<OptionResponse> options) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
        this.options = options;
    }
}
