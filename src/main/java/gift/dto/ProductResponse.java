package gift.dto;

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
    private Long categoryId;
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
